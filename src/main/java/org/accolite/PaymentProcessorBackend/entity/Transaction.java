package org.accolite.PaymentProcessorBackend.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long transactionId;
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    PaymentMode paymentMode;
    @Enumerated(EnumType.STRING)
    TransactionStatus status;
    Date date;
    int userId;
    int vendorId;
    @Version
    Long version;
}
