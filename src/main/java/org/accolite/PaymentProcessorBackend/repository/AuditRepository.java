package org.accolite.PaymentProcessorBackend.repository;

import org.accolite.PaymentProcessorBackend.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
