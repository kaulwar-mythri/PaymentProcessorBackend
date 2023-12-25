package org.accolite.PaymentProcessorBackend.serviceImpl;

import org.accolite.PaymentProcessorBackend.entity.*;
import org.accolite.PaymentProcessorBackend.repository.*;
import org.accolite.PaymentProcessorBackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    VendorRepository vendorRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WalletRepository walletRepository;

    @Override
    public ResponseEntity<String> createAdmin(Admin admin) {
//        admin.setCompany_wallet(new Wallet());
        adminRepository.save(admin);
        return ResponseEntity.ok("Admin created");
    }

    @Override
    public ResponseEntity<String> approveUser(int adminId, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with the above id not found error"));
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin with the above id not found error"));

        //if(user.getIsEnrolled()) {
            user.setApproval_status(true);
            user.setApproval_time(new Date(System.currentTimeMillis()));
            Wallet wallet = user.getWallet();
            if(wallet == null) {
                wallet = new Wallet();
                wallet.setBalance(BigDecimal.ZERO);
                wallet.setOfflineBalance(BigDecimal.ZERO);
                walletRepository.save(wallet);
                user.setWallet(wallet);
            }
            userRepository.save(user);
            return ResponseEntity.ok("User approved by admin");
        //}
        //return ResponseEntity.badRequest().body("User did not request enrollement yet");
    }

    @Override
    public ResponseEntity<String> reviewTransaction(int adminId, int transactionId, Boolean approve) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin with the above id not found error"));
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction with the given transaction id not found"));

        if(approve) {
            Vendor vendor = vendorRepository.findById(transaction.getVendorId()).orElseThrow(() -> new RuntimeException("Vendor with the given id not found error"));

            Wallet vendorWallet = vendor.getStore_wallet();
            vendorWallet.setOfflineBalance(vendorWallet.getOfflineBalance().add(transaction.getAmount()));
            walletRepository.save(vendorWallet);

            transaction.setStatus(TransactionStatus.APPROVED);
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Offline Transaction approved by admin");
        } else {
            User user = userRepository.findById(transaction.getUserId()).orElseThrow(() -> new RuntimeException("User with the given id not found error"));

            Wallet userWallet = user.getWallet();
            userWallet.setOfflineBalance(userWallet.getOfflineBalance().add(transaction.getAmount()));
            walletRepository.save(userWallet);

            transaction.setStatus(TransactionStatus.REJECTED);
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Offline Transaction rejected by admin");
        }
    }

    @Override
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        return ResponseEntity.ok(transactionRepository.findByStatus(TransactionStatus.FLAGGED).get());
    }

    @Override
    public ResponseEntity<String> approveVendor(int adminId, int vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor with the above id not found error"));
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin with the above id not found error"));

        vendor.setApproval_status(true);
        Wallet wallet = vendor.getStore_wallet();
        if(wallet == null) {
            wallet = new Wallet();
            vendor.setStore_wallet(wallet);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setOfflineBalance(BigDecimal.ZERO);
            walletRepository.save(wallet);
        }
        vendorRepository.save(vendor);
        return ResponseEntity.ok("Vendor approved by admin");
    }
}
