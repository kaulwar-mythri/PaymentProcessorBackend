package org.accolite.PaymentProcessorBackend.service;

import lombok.RequiredArgsConstructor;
import org.accolite.PaymentProcessorBackend.auth.AuthRequest;
import org.accolite.PaymentProcessorBackend.auth.AuthenticationResponse;
import org.accolite.PaymentProcessorBackend.auth.RegisterRequest;
import org.accolite.PaymentProcessorBackend.entity.Role;
import org.accolite.PaymentProcessorBackend.entity.User;
import org.accolite.PaymentProcessorBackend.repository.UserRepository;
import org.accolite.PaymentProcessorBackend.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .approval_status(false)
                .isEnrolled(false)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateJWTToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateJWTToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}