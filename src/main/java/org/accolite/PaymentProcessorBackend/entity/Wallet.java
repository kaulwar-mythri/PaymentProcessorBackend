package org.accolite.PaymentProcessorBackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int walletId;
    BigDecimal offlineBalance;
    BigDecimal balance;

    @ElementCollection
    @CollectionTable(name = "payment_codes", joinColumns = @JoinColumn(name = "walletId"))
    @Column(name = "code")
    @Builder.Default
    Set<String> payment_codes = new HashSet<>();

    public Wallet(int walletId) {
        this.walletId = walletId;
    }
}

