package org.accolite.PaymentProcessorBackend.controller;

import lombok.RequiredArgsConstructor;
import org.accolite.PaymentProcessorBackend.auth.AuthRequest;
import org.accolite.PaymentProcessorBackend.auth.AuthenticationResponse;
import org.accolite.PaymentProcessorBackend.auth.RegisterRequest;
import org.accolite.PaymentProcessorBackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authenticationService.register(req));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authenticationService.authenticate(req));
    }
}
