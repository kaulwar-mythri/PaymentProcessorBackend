package org.accolite.PaymentProcessorBackend.controller;

import org.accolite.PaymentProcessorBackend.entity.Admin;
import org.accolite.PaymentProcessorBackend.entity.Transaction;
import org.accolite.PaymentProcessorBackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestBody Admin admin) {
        return adminService.createAdmin(admin);
    }
    @PostMapping("/approve-user/{adminId}/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable int adminId, @PathVariable int userId) {
        return adminService.approveUser(adminId, userId);
    }
    @PostMapping("/approve-vendor/{adminId}/{vendorId}")
    public ResponseEntity<String> approveVendor(@PathVariable int vendorId, @PathVariable int adminId) {
        return adminService.approveVendor(adminId, vendorId);
    }

    @GetMapping("/get-flagged-transactions")
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        return adminService.getFlaggedTransactions();
    }

    @PostMapping("/review-transaction/{adminId}/{transactionId}")
    public ResponseEntity<String> reviewTransaction(@PathVariable int adminId, @PathVariable int transactionId, @RequestParam Boolean approve) {
        return adminService.reviewTransaction(adminId, transactionId, approve);
    }
}
