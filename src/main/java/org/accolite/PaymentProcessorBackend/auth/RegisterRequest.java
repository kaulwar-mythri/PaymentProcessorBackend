package org.accolite.PaymentProcessorBackend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.accolite.PaymentProcessorBackend.entity.Role;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    String username;
    String name;
    String password;
    Role role;
}
