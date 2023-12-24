package org.accolite.PaymentProcessorBackend.serviceImpl;

import org.accolite.PaymentProcessorBackend.entity.*;
import org.accolite.PaymentProcessorBackend.exceptions.UserNotFoundException;
import org.accolite.PaymentProcessorBackend.repository.*;
import org.accolite.PaymentProcessorBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final int EARTH_RADIUS = 6371;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public ResponseEntity<String> enrollForOfflinePayment(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with given ID not found"));
        if(!user.getIsEnrolled())
            user.setIsEnrolled(true);
        else throw new RuntimeException("User already enrolled");

        return ResponseEntity.ok("Successfully requested enrollment");
    }

    @Override
    public ResponseEntity<String> addFundsToWallet(int id, long amount) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with given id not found"));

        checkIfUserCanAccessFunctionalities(user);

        if(user.getWallet() != null) {
            System.out.println("Wallet found");
            Wallet wallet = user.getWallet();
            wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(amount)));
            walletRepository.save(wallet);
        }
        return ResponseEntity.ok("Successfully added funds to wallet");
    }

    private void checkIfUserCanAccessFunctionalities(User user) {
        if(!user.getApproval_status())
            throw new RuntimeException("User is yet to be approved");
        Date date = new Date();
        if(System.currentTimeMillis() - user.getApproval_time().getTime() < 1000 * 60)
            throw new RuntimeException("You need to wait for 15 minutes to make use of the requested functionalities");
    }

    @Override
    public ResponseEntity<String> transferMoneyToOffline(int userId, long amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with given id not found"));

        checkIfUserCanAccessFunctionalities(user);

        Wallet wallet = user.getWallet();
        if(wallet.getPayment_codes().isEmpty())
            wallet.setPayment_codes(generateOfflinePaymentCodes(wallet));

        wallet.setOfflineBalance(wallet.getOfflineBalance().add(BigDecimal.valueOf(amount)));
        wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(amount)));
        walletRepository.save(wallet);

        return ResponseEntity.ok("Allocated offline balance");
    }

    @Override
    public ResponseEntity<Set<String>> getOfflineCodes(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with given id not found"));

        checkIfUserCanAccessFunctionalities(user);

        Wallet wallet = user.getWallet();
        if(wallet.getPayment_codes().isEmpty())
            return ResponseEntity.ok(new HashSet<>());

        return ResponseEntity.ok(wallet.getPayment_codes());
    }

    @Override
    public ResponseEntity<String> makeOnlinePayment(int userId, int vendorId, long amount, String otp) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with given id not found"));

        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new RuntimeException("Vendor with the above id not found error"));

        checkIfUserCanAccessFunctionalities(user);

        if(user.getOtp().compareTo(otp) == 0) {

            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setVendorId(vendorId);
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setPaymentMode(PaymentMode.ONLINE);
            transaction.setStatus(TransactionStatus.APPROVED);
            transaction.setAmount(BigDecimal.valueOf(amount));

            Wallet userWallet = user.getWallet();
            userWallet.setBalance(userWallet.getBalance().subtract(BigDecimal.valueOf(amount)));
            walletRepository.save(userWallet);

            Wallet vendorWallet = vendor.getStore_wallet();
            vendorWallet.setBalance(vendorWallet.getBalance().add(BigDecimal.valueOf(amount)));
            walletRepository.save(vendorWallet);


            user.setOtp(null);

            try {
                transactionRepository.save(transaction);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Concurrent modification detected");
                e.printStackTrace();
            }

            return ResponseEntity.ok("Transaction successfull");
        }
        return ResponseEntity.badRequest().body("Wrong OTP provided");
    }

    @Override
    public ResponseEntity<String> makeOfflinePayment(int userId, int vendorId, long amount, double latitude, double longitude, String code) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with given id not found"));

        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new RuntimeException("Vendor with the above id not found error"));

        checkIfUserCanAccessFunctionalities(user);

        Wallet userWallet = user.getWallet();

        if(userWallet.getPayment_codes().contains(code)) {
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setVendorId(vendorId);
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setPaymentMode(PaymentMode.OFFLINE);
            transaction.setAmount(BigDecimal.valueOf(amount));

            userWallet.setBalance(userWallet.getBalance().subtract(BigDecimal.valueOf(amount)));
            walletRepository.save(userWallet);

            if(isWithin20KMRadius(latitude, longitude, vendor.getLatitude(), vendor.getLongitude())) {
                transaction.setStatus(TransactionStatus.APPROVED);

                Wallet vendorWallet = vendor.getStore_wallet();
                vendorWallet.setBalance(vendorWallet.getBalance().add(BigDecimal.valueOf(amount)));
                walletRepository.save(vendorWallet);

                try {
                    transactionRepository.save(transaction);
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("Concurrent modification detected");
                    e.printStackTrace();
                }
                return ResponseEntity.ok("Transaction successfull");
            } else {
                transaction.setStatus(TransactionStatus.FLAGGED);

                try {
                    transactionRepository.save(transaction);
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("Concurrent modification detected");
                    e.printStackTrace();
                }
                return ResponseEntity.ok("Transaction flagged");
            }
        } else {
            return ResponseEntity.badRequest().body("The given code does not match any of the offline codes generated for this userId");
        }

    }

    @Override
    public ResponseEntity<String> generateOTP(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with given id not found"));
        StringBuilder otp = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for(int i=0; i<6; i++) {
            int index = random.nextInt(10);
            otp.append(index);
        }
        user.setOtp(otp.toString());
        userRepository.save(user);
        return ResponseEntity.ok(otp.toString());
    }

    private boolean isWithin20KMRadius(double latitude, double longitude, double latitude1, double longitude1) {
        double dLat = Math.toRadians(latitude1 - latitude);
        double dLon = Math.toRadians(longitude1 - longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude1)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c <= 20;
    }

    public Set<String> generateOfflinePaymentCodes(Wallet wallet) {
        if(wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Insufficient balance to generate offline payment codes");

        Set<String> offline_codes = new HashSet<>();
        for(int i=0; i<5; i++) {
            String code = UUID.randomUUID().toString();
            offline_codes.add(code);
        }
        return offline_codes;
    }

}
