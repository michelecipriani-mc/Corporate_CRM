package com.crm.corporate_crm.security.service;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.modulith.moments.support.Now;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.NuovoUtenteDto;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.dto.AuthRequest;
import com.crm.corporate_crm.security.dto.AuthResponse;
import com.crm.corporate_crm.security.dto.RegisterRequest;
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


    private final String ruoloIniziale = "UTENTE";


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

        // ID del token refresh
        String tid = UUID.randomUUID().toString();

        // Salva id nel database
        // refreshtokenrepository.add() ...

        String refreshToken = jwtService.generateRefreshToken(user, tid);

        if(tokenRevocatoService.isPresentToken(accesstoken)){
            throw new RuntimeException("Il token di accesso generato non è valido!");
        }

        // Salva il refresh token nel DB
        utenteServiceApi.updateRefreshToken(user.getId(), refreshToken);

        // Restituisce il token al client come risposta JSON
        return new AuthResponse(accesstoken, refreshToken);
    }

    public AuthResponse refresh (String accessToken, String refreshToken) {

        // verifico che l'accessToken non sia nullo o vuoto
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access Token non può essere nullo o vuoto.");
        }

        //recupero la mail dell'utente attraverso la decodifica del token
        String email = jwtService.extractUsernameAllowExpired(accessToken);

        //faccio la ricerca dell'utente via mail, altrimenti utente non trovato
        //UtenteDto utente = utenteServiceApi.findByEmail(email)
        //        .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        CustomPrincipal utente = (CustomPrincipal) userDetailsService.loadUserByEmail(email);

        /** Se quindi il token di accesso e valido e il refreshToken corrisponde allora genero un newAccessToken e un newRefreshToken */
        System.out.println("Refresh dal front: " + refreshToken + "\n");
        System.out.println("Refresh dal DB: " + utente.getRefreshToken());
        if (utente.getRefreshToken() != null && utente.getRefreshToken().trim().equals(refreshToken.trim())) {
            String newAccessToken = jwtService.generateToken(utente);
            //verifico che il nuovo token per errore non sia stato già generato e che sia valido
            if(tokenRevocatoService.isPresentToken(newAccessToken)){
                throw new RuntimeException("Il token di accesso generato non è valido!");
            }
            // ID del token refresh
            String tid = UUID.randomUUID().toString();
            String newRefreshToken = jwtService.generateRefreshToken(utente, tid);
            // Salva il refresh token nel DB
            utenteServiceApi.updateRefreshToken(utente.getId(), newRefreshToken);

            // Salva l'access token vecchio in blacklist
            tokenRevocatoService.blackListToken(accessToken, /*jwtService.extractExpiration(accessToken).toInstant()*/Instant.now());

            return new AuthResponse(newAccessToken, newRefreshToken);
        } else {
            throw new RuntimeException("Refresh Token non valido");
        }
    }

    public String logout(String accessToken, String refreshToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access Token non può essere nullo o vuoto.");
        }

        // recupero email dal token di accesso
        String email = jwtService.extractUsername(accessToken);
        CustomPrincipal utente = (CustomPrincipal) userDetailsService.loadUserByEmail(email);

        // annullo il refresh token
        utenteServiceApi.updateRefreshToken(utente.getId(), null);

        // metto l’access token in blacklist fino alla sua scadenza
        tokenRevocatoService.blackListToken(accessToken,jwtService.extractExpiration(accessToken).toInstant());

        return "Logout effettuato correttamente!";
    }

    public Map<String, String> register (RegisterRequest request) {

        // Controlla se l'email è già in uso
        if (utenteServiceApi.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Utente già registrato con questa email");
        }

        // Crea richiesta nuovo utente per anagrafica
        NuovoUtenteDto richiestaNuovoUtente = 
                new NuovoUtenteDto(request.getUsername(), 
                        request.getEmail(), 

                        // Hashing password
                        passwordEncoder.encode(request.getPassword()), 

                        // Assegnazione automatica del ruolo Utente
                        ruoloIniziale);

        // Salva nuovo utente
        utenteServiceApi.save(richiestaNuovoUtente);

        // Restituisce un oggetto JSON con un messaggio
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registrazione effettuata con successo");

        // Invia conferma di registrazione
        return response;
    }
}
