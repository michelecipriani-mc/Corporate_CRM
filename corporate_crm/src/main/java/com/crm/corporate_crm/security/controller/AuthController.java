package com.crm.corporate_crm.security.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.dto.AuthRequest;
import com.crm.corporate_crm.security.dto.AuthResponse;
import com.crm.corporate_crm.security.api.dto.RegisterRequest;
import com.crm.corporate_crm.security.service.AuthService;
import com.crm.corporate_crm.security.service.JwtService;

import lombok.RequiredArgsConstructor;

/**
 * Controller REST per la gestione dell'autenticazione tramite JWT.
 * Espone un endpoint POST /auth/login per autenticare un utente
 * e restituire un token JWT valido.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    // private final AuthenticationManager authManager;
    // private final JwtService jwtService;
    // private final UserDetailsService userDetailsService;
    // private final PasswordEncoder passwordEncoder;
    // // private final UtenteRepository utenteRepository; // rimozione
    // private final UtenteServiceApi utenteServiceApi;

    /**
     * Endpoint POST /auth/login
     * Riceve username e password (AuthRequest),
     * autentica l'utente e restituisce un token JWT (AuthResponse).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody AuthRequest request) {

        return ResponseEntity.ok(authService.login(request));

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh (@RequestBody Map<String, String> request) {

        return ResponseEntity.ok(authService.refresh(request));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (@RequestBody Map<String, String> request) {

        return ResponseEntity.ok(authService.logout(request));

    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@Validated @RequestBody RegisterRequest request) {

        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }
}