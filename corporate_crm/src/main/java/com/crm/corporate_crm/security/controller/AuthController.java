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

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    // private final UtenteRepository utenteRepository; // rimozione
    private final UtenteServiceApi utenteServiceApi;

    /**
     * Endpoint POST /auth/login
     * Riceve username e password (AuthRequest),
     * autentica l'utente e restituisce un token JWT (AuthResponse).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // Autentica l'utente usando AuthenticationManager.
        // Se le credenziali sono errate, Spring Security lancia un'eccezione 401
        // automaticamente.
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Recupera l'utente spring security (UserDetails) per ottenere i ruoli 
        // e firmare correttamente il JWT.
        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        // Genera il token JWT a partire dai dati dell'utente
        String accesstoken = jwtService.generateToken(user);
        String refreshToken = UUID.randomUUID().toString();

        // Salva il refresh token nel DB
        utenteServiceApi.updateRefreshToken(user.getUsername(), refreshToken);

        // Restituisce il token al client come risposta JSON
        return ResponseEntity.ok(new AuthResponse(accesstoken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh (@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.get("email"));
        UtenteDto utente = utenteServiceApi.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (utente.getRefreshToken() != null && utente.getRefreshToken().equals(refreshToken)) {
            String newToken = jwtService.generateToken(userDetails);
            // Salva il refresh token nel DB
            utenteServiceApi.updateRefreshToken(utente.getEmail(), newToken);
            return ResponseEntity.ok(new AuthResponse(newToken, refreshToken));
        } else {
            throw new RuntimeException("Refresh Token non valido");
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (@RequestBody Map<String, String> request) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.get("email"));
        utenteServiceApi.updateRefreshToken(userDetails.getUsername(), null);
        return ResponseEntity.ok("Logout effettuato.");

    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@Validated @RequestBody RegisterRequest request) {

        // Controlla se l'email è già in uso
        if (utenteServiceApi.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Utente già registrato con questa email");
        }

        // Hashing password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // Salva nuovo utente
        utenteServiceApi.save(request);

        // Invia conferma
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registrazione completata con successo");
    }
}