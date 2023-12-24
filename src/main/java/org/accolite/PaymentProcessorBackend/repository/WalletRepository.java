package org.accolite.PaymentProcessorBackend.repository;

import org.accolite.PaymentProcessorBackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
}
