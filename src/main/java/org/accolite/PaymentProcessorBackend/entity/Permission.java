package org.accolite.PaymentProcessorBackend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    VENDOR_READ("management:read"),
    VENDOR_UPDATE("management:update"),
    VENDOR_CREATE("management:create"),
    VENDOR_DELETE("management:delete");

    @Getter
    private final String permission;
}
