package org.accolite.PaymentProcessorBackend.repository;

import org.accolite.PaymentProcessorBackend.entity.Vendor;
import org.accolite.PaymentProcessorBackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Boolean existsByUsername(String username);
}
