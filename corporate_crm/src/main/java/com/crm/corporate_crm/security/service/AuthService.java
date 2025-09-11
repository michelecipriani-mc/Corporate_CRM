package com.crm.corporate_crm.security.service;

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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.api.dto.RegisterRequest;
import com.crm.corporate_crm.security.dto.AuthRequest;
import com.crm.corporate_crm.security.dto.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    // private final UtenteRepository utenteRepository; // rimozione
    private final UtenteServiceApi utenteServiceApi;


    public AuthResponse login (AuthRequest request) {
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
        return new AuthResponse(accesstoken, refreshToken);
    }

    public AuthResponse refresh (Map<String, String> request) {

        String refreshToken = request.get("refreshToken");
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.get("email"));
        UtenteDto utente = utenteServiceApi.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (utente.getRefreshToken() != null && utente.getRefreshToken().equals(refreshToken)) {
            String newToken = jwtService.generateToken(userDetails);
            // Salva il refresh token nel DB
            utenteServiceApi.updateRefreshToken(utente.getEmail(), newToken);
            return new AuthResponse(newToken, refreshToken);
        } else {
            throw new RuntimeException("Refresh Token non valido");
        }

    }

    public String logout (Map<String, String> request) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.get("email"));
        utenteServiceApi.updateRefreshToken(userDetails.getUsername(), null);
        return "Logout effettuato correttamente!";

    }

    public String register (RegisterRequest request) {

        // Controlla se l'email è già in uso
        if (utenteServiceApi.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Utente già registrato con questa email");
        }

        // Hashing password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // Salva nuovo utente
        utenteServiceApi.save(request);

        // Invia conferma
        return "Registrazione completata con successo";
    }
}
