package org.accolite.PaymentProcessorBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int vendorId;
    String username;
    Boolean approval_status;
    double latitude;
    double longitude;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_walletId", referencedColumnName = "walletId")
    Wallet store_wallet;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_walletId", referencedColumnName = "walletId")
    Wallet personal_wallet;
}
