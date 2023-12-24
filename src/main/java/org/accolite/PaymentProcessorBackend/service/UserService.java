package org.accolite.PaymentProcessorBackend.service;


import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface UserService {
    public ResponseEntity<String> enrollForOfflinePayment(int id);

    public ResponseEntity<String> addFundsToWallet(int id, long amount);

    public ResponseEntity<String> transferMoneyToOffline(int userId, long amount);

    public ResponseEntity<Set<String>> getOfflineCodes(int userId);

    public ResponseEntity<String> makeOnlinePayment(int userId, int vendorId, long amount, String otp);

    public ResponseEntity<String> makeOfflinePayment(int userId, int vendorId, long amount, double latitude, double longitude, String code);

    public ResponseEntity<String> generateOTP(int userId);
}
