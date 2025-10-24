package com.crm.corporate_crm.security.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.corporate_crm.security.dto.AuthRequest;
import com.crm.corporate_crm.security.dto.AuthResponse;
import com.crm.corporate_crm.security.dto.RegisterRequest;
import com.crm.corporate_crm.security.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST per la gestione dell'autenticazione tramite JWT.
 * Espone un endpoint POST /auth/login per autenticare un utente
 * e restituire un token JWT valido.
 */
@RestController
@RequestMapping("/auth")
// Permetti l'accesso da URL Angular
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint POST /auth/login
     * Riceve username e password (AuthRequest),
     * autentica l'utente e restituisce un token JWT (AuthResponse).
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login (@RequestBody AuthRequest request, HttpServletResponse response) {


        AuthResponse tokens = authService.login(request);
        
        // Crea il cookie HttpOnly per il refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth") // Cookie path for all /auth endpoints
                .maxAge(7 * 24 * 60 * 60) // 7 giorni
                .build();

        // Aggiunge il cookie alla risposta HTTP
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Crea un oggetto JSON da restituire
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokens.getAccessToken());

        // Restituisce l'access token nel corpo della risposta
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh (
            @CookieValue(name = "refreshToken") String refreshToken, 
            HttpServletRequest request, 
            HttpServletResponse response) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new IllegalArgumentException("Header Authorization mancante o non valido.");
        }
        
        String accessToken = authHeader.substring(7);

        // Chiama il servizio con i token estratti
        AuthResponse tokens = authService.refresh(accessToken, refreshToken);
        
        // Crea un nuovo cookie HttpOnly per il nuovo refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth")
                .maxAge(7 * 24 * 60 * 60) // 7 giorni
                .build();

        // Aggiunge il nuovo cookie alla risposta HTTP
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Restituisce il nuovo access token nel corpo della risposta
        return ResponseEntity.ok(tokens.getAccessToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header Authorization mancante o non valido.");
        }
        String accessToken = authHeader.substring(7);

        String message = authService.logout(accessToken, refreshToken);

        // invalida il cookie sul client
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth")
                .maxAge(0) // elimina subito
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(message);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register (@Validated @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}