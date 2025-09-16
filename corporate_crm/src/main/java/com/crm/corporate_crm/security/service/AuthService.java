package com.crm.corporate_crm.security.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.RegisterRequest;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.dto.AuthRequest;
import com.crm.corporate_crm.security.dto.AuthResponse;
import com.crm.corporate_crm.security.model.CustomPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRevocatoService tokenRevocatoService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UtenteServiceApi utenteServiceApi;
    private final CustomUserDetailsService userDetailsService;


    public AuthResponse login (AuthRequest request) {
        // Autentica l'utente usando AuthenticationManager.
        // Se le credenziali sono errate, Spring Security lancia un'eccezione 401
        // automaticamente.
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Recupera l'utente spring security (UserDetails) per ottenere i ruoli 
        // e firmare correttamente il JWT.
        // UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        // Genera il token JWT a partire dai dati dell'utente
        String accesstoken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        if(tokenRevocatoService.isPresentToken(accesstoken)){
            throw new RuntimeException("Il token di accesso generato non è valido!");
        }

        // Salva il refresh token nel DB
        utenteServiceApi.updateRefreshToken(user.getId(), refreshToken);

        // Restituisce il token al client come risposta JSON
        return new AuthResponse(accesstoken, refreshToken);
    }

    public AuthResponse refresh (Map<String, String> request) {

        // Recupero l'accessToken registrato
        String accessToken = request.get("accessToken");
        // Recupero il refreshToken registrato
        String refreshToken = request.get("refreshToken");
        //verifico che l'accessToken non sia nullo o vuoto
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access Token non può essere nullo o vuoto.");
        }

        //recupero la mail dell'utente attraverso la decodifica del token
        String email = jwtService.extractUsername(accessToken);

        //faccio la ricerca dell'utente via mail, altrimenti utente non trovato
        //UtenteDto utente = utenteServiceApi.findByEmail(email)
        //        .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        CustomPrincipal utente = (CustomPrincipal) userDetailsService.loadUserByEmail(email);

        /** Se quindi il token di accesso e valido e il refreshToken corrisponde allora genero un newAccessToken e un newRefreshToken */
        if (utente.getRefreshToken() != null && utente.getRefreshToken().equals(refreshToken)) {
            String newAccessToken = jwtService.generateToken(utente);
            //verifico che il nuovo token per errore non sia stato già generato e che sia valido
            if(tokenRevocatoService.isPresentToken(newAccessToken)){
                throw new RuntimeException("Il token di accesso generato non è valido!");
            }
            String newRefreshToken = jwtService.generateRefreshToken(utente);
            // Salva il refresh token nel DB
            utenteServiceApi.updateRefreshToken(utente.getId(), newRefreshToken);

            // Salva l'access token vecchio in blacklist
            tokenRevocatoService.blackListToken(accessToken, jwtService.extractExpiration(accessToken).toInstant());

            return new AuthResponse(newAccessToken, newRefreshToken);
        } else {
            throw new RuntimeException("Refresh Token non valido");
        }
    }

    public String logout (Map<String, String> request) {
        // Recupero l'accessToken registrato
        String accessToken = request.get("accessToken");
        //verifico che l'accessToken non sia nullo o vuoto
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access Token non può essere nullo o vuoto.");
        }

        //recupero la mail dell'utente attraverso la decodifica del token
        String email = jwtService.extractUsername(accessToken);
        //faccio la ricerca dell'utente via mail, altrimenti utente non trovato
        //UtenteDto utente = utenteServiceApi.findByEmail(email)
        //        .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        CustomPrincipal utente = (CustomPrincipal) userDetailsService.loadUserByEmail(email);

        //Annullo il refresh token
        utenteServiceApi.updateRefreshToken(utente.getId(), null);

        //Annullo l'access token
        tokenRevocatoService.blackListToken(accessToken, jwtService.extractExpiration(accessToken).toInstant());
    
        return "Logout effettuato correttamente!";
    }

    public AuthResponse register (RegisterRequest request) {

        // Controlla se l'email è già in uso
        if (utenteServiceApi.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Utente già registrato con questa email");
        }
        
        //Creo un nuovo oggetto AuthRequest prima di codificarlo, utile per il richiamo del metodo login
        AuthRequest newRequest = new AuthRequest(request.getEmail(), request.getPassword());

        // Assegnazione automatica del ruolo Utente
        request.setRuolo("UTENTE");
        // Hashing password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // Salva nuovo utente
        utenteServiceApi.save(request);

        // Invia conferma di registrazione effettuando direttamente il login al CRM
        return login(newRequest);
    }
}
