package org.accolite.PaymentProcessorBackend.controller;

import org.accolite.PaymentProcessorBackend.auth.AuthenticationResponse;
import org.accolite.PaymentProcessorBackend.entity.Vendor;
import org.accolite.PaymentProcessorBackend.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {
    @Autowired
    VendorService vendorService;
    @PostMapping("/auth")
    public AuthenticationResponse registerVendor(@RequestBody Vendor vendor) {
        return vendorService.registerVendor(vendor);
    }

    @PostMapping("/transfer-to-personal-wallet/{vendorId}")
    public ResponseEntity<String> transferToPersonalWallet(@PathVariable int vendorId, @RequestParam int personal_walletId, @RequestParam int amount) {
        return vendorService.transferToPersonalWallet(vendorId, personal_walletId, amount);
    }

    //requestOfflinePayment
}
