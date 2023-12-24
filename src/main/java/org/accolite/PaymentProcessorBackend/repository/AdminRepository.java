package org.accolite.PaymentProcessorBackend.repository;

import org.accolite.PaymentProcessorBackend.entity.Admin;
import org.accolite.PaymentProcessorBackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
