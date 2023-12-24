package org.accolite.PaymentProcessorBackend.serviceImpl;

import org.accolite.PaymentProcessorBackend.auth.AuthenticationResponse;
import org.accolite.PaymentProcessorBackend.entity.Vendor;
import org.accolite.PaymentProcessorBackend.entity.Wallet;
import org.accolite.PaymentProcessorBackend.repository.AdminRepository;
import org.accolite.PaymentProcessorBackend.repository.VendorRepository;
import org.accolite.PaymentProcessorBackend.security.JWTService;
import org.accolite.PaymentProcessorBackend.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    VendorRepository vendorRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registerVendor(Vendor vendor) {
        if(vendorRepository.existsByUsername(vendor.getUsername())) {

        }
        vendor.setApproval_status(false);
        vendorRepository.save(vendor);
        var jwtToken = jwtService.generateVendorJWT(vendor);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public ResponseEntity<String> transferToPersonalWallet(int vendorId, int personalWalletId, int amount) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor with the above id not found error"));
        if(vendor.getStore_wallet() == null)
            return ResponseEntity.badRequest().body("Looks like vendor is still not approved by admin");

        Wallet personWallet = vendor.getPersonal_wallet();
        if(personWallet == null) {
            personWallet = new Wallet(personalWalletId);
            vendor.setPersonal_wallet(personWallet);
        }

        vendor.getStore_wallet().setBalance(vendor.getStore_wallet().getBalance().subtract(BigDecimal.valueOf(amount)));
        vendor.getPersonal_wallet().setBalance(vendor.getPersonal_wallet().getBalance().add(BigDecimal.valueOf(amount)));

        return ResponseEntity.ok("Successfullt transferred amount to personal wallet");
    }
}
