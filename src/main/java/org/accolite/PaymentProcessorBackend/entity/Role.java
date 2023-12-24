package org.accolite.PaymentProcessorBackend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public enum Role {
    USER,
    ADMIN,
    VENDOR
}
