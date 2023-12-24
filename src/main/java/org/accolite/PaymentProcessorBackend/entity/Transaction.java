package org.accolite.PaymentProcessorBackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Transaction {
    @Id
    Long transactionId;
    BigDecimal amount;

    PaymentMode paymentMode;
    TransactionStatus status;
    Date date;
    int userId;
    int vendorId;
    @Version
    Long version;
}
