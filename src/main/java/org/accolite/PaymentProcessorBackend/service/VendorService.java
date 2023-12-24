package org.accolite.PaymentProcessorBackend.service;

import org.accolite.PaymentProcessorBackend.auth.AuthenticationResponse;
import org.accolite.PaymentProcessorBackend.entity.Vendor;
import org.springframework.http.ResponseEntity;


public interface VendorService {

    public AuthenticationResponse registerVendor(Vendor vendor);

    public ResponseEntity<String> transferToPersonalWallet(int vendorId, int personalWalletId, int amount);
}
