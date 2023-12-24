package org.accolite.PaymentProcessorBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int adminId;
    String username;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_walletId", referencedColumnName = "walletId")
    Wallet company_wallet;
}
