package org.accolite.PaymentProcessorBackend.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.accolite.PaymentProcessorBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/enroll/{userId}")
    public ResponseEntity<String> enrollForOfflinePayment(@PathVariable int userId) {
        return userService.enrollForOfflinePayment(userId);
    }

    @PostMapping("/add-funds-to-wallet/{userId}/{amount}")
    public ResponseEntity<String> addFundsToWallet(@PathVariable int userId, @PathVariable long amount) {
        return userService.addFundsToWallet(userId, amount);
    }


    @PostMapping("/allocate-offline-balance/{userId}/{amount}")
    public ResponseEntity<String> transferMoneyToOffline(@PathVariable int userId, @PathVariable long amount) {
        return userService.transferMoneyToOffline(userId, amount);
    }

    @PostMapping("/get-offline-codes/{userId}")
    public ResponseEntity<Set<String>> getOfflineCodes(@PathVariable int userId) {
        return userService.getOfflineCodes(userId);
    }

    @PostMapping("/make-online-payment/{userId}/{vendorId}/{amount}/{otp}")
    public ResponseEntity<String> makeOnlinePayment(@PathVariable int userId, @PathVariable int vendorId, @PathVariable long amount, @PathVariable String otp) {
        return userService.makeOnlinePayment(userId, vendorId, amount, otp);
    }

    @PostMapping("/make-offline-payment/")
    public ResponseEntity<String> makeOfflinePayment(@RequestBody OfflinePaymentRequest paymentRequest) {
        return userService.makeOfflinePayment(paymentRequest.getUserId(), paymentRequest.getVendorId(), paymentRequest.getAmount(), paymentRequest.getLatitude(), paymentRequest.getLongitude(), paymentRequest.getCode());
    }

    @GetMapping("/generateOTP/{userId}")
    public ResponseEntity<String> generateOTP(@PathVariable int userId) {
        System.out.println("in otp generator");
        return userService.generateOTP(userId);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class OfflinePaymentRequest {
        int userId;
        int vendorId;
        long amount;
        double latitude;
        double longitude;
        String code;
    }
}
