package org.accolite.PaymentProcessorBackend.service;

import org.accolite.PaymentProcessorBackend.entity.Admin;
import org.accolite.PaymentProcessorBackend.entity.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    public ResponseEntity<String> approveVendor(int adminId, int vendorId);

    public ResponseEntity<String> createAdmin(Admin admin);

    public ResponseEntity<String> approveUser(int adminId, int userId);

    public ResponseEntity<String> reviewTransaction(int adminId, int transactionId, Boolean approve);

    public ResponseEntity<List<Transaction>> getFlaggedTransactions();
}
