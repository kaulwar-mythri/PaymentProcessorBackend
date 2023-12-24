package org.accolite.PaymentProcessorBackend.repository;

import org.accolite.PaymentProcessorBackend.entity.Transaction;
import org.accolite.PaymentProcessorBackend.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Optional<List<Transaction>> findByStatus(TransactionStatus transactionStatus);
}
