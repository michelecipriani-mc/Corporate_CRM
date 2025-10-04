package com.crm.corporate_crm.security.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crm.corporate_crm.common.events.UtenteRegistrato;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.dto.AuthRequest;
import com.crm.corporate_crm.security.dto.AuthResponse;
import com.crm.corporate_crm.security.dto.RegisterRequest;
import com.crm.corporate_crm.security.model.Credenziali;
import com.crm.corporate_crm.security.model.CustomPrincipal;
import com.crm.corporate_crm.security.model.Ruolo;
import com.crm.corporate_crm.security.repository.AuthRepository;
import com.crm.corporate_crm.security.repository.RuoloRepository;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRevocatoService tokenRevocatoService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final AuthRepository authRepository;

    private final RuoloRepository ruoloRepository;

    private final CustomUserDetailsService userDetailsService;
    private final ApplicationEventPublisher events;

    private static final String RUOLO_BASE = "UTENTE";

    @Transactional
    public AuthResponse login(AuthRequest request) {
        // Autentica l'utente usando AuthenticationManager.
        // Se le credenziali sono errate, Spring Security lancia un'eccezione 401
        // automaticamente.
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Recupera l'utente spring security (UserDetails) per ottenere i ruoli
        // e firmare correttamente il JWT.
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        // Genera l'access token JWT a partire dai dati dell'utente
        String accesstoken = jwtService.generateToken(user);

        // Controlla se l'access token è in blacklist
        if (tokenRevocatoService.isTokenPresent(accesstoken)) {
            throw new InvalidAccessTokenException("Il token di accesso generato non è valido!");
        }

        // Genera id per token refresh
        String tid = UUID.randomUUID().toString();

        // Genera token refresh a partire da id
        String refreshToken = jwtService.generateRefreshToken(user, tid);

        // Salva id del token refresh nel DB
        authRepository.setTidById(tid, user.getId());

        // Restituisce il token al client come risposta JSON
        return new AuthResponse(accesstoken, refreshToken);
    }

    /**
     * Esegue il refresh dei token di autenticazione utilizzando un access token
     * valido ma anche scaduto e un refresh token valido non scaduto.
     * <p>
     * Questo metodo esegue in modo atomico, all'interno di un contesto
     * transazionale, le seguenti operazioni:
     * <ul>
     * <li>Verifica che accessToken e refreshToken non siano nulli o vuoti.</li>
     * <li>Controlla che l'access token non sia presente nella blacklist,
     * rifiutandolo in caso contrario.</li>
     * <li>Estrae l'ID utente dall'access token, anche se scaduto.</li>
     * <li>Carica i dettagli dell'utente dal database tramite l'ID.</li>
     * <li>Verifica che il refresh token sia firmato correttamente, non scaduto e
     * che l'ID del token (tid) corrisponda a quello salvato.</li>
     * <li>Genera un nuovo access token e verifica che non sia già stato invalidato
     * (blacklistato).</li>
     * <li>Genera un nuovo refresh token con un nuovo ID univoco (tid) e salva il
     * nuovo tid nel database.</li>
     * <li>Aggiunge l'access token precedente alla blacklist per impedirne il
     * riuso.</li>
     * </ul>
     *
     * @param accessToken  il token di accesso corrente, che può essere scaduto ma
     *                     non blacklistato né nullo/vuoto
     * @param refreshToken il token di refresh valido associato all'utente, non
     *                     nullo né vuoto
     * @return un {@link AuthResponse} contenente i nuovi token di accesso e refresh
     * @throws IllegalArgumentException     se uno dei token è nullo o vuoto
     * @throws InvalidAccessTokenException  se l'access token è blacklistato o se il
     *                                      nuovo access token generato non è valido
     * @throws InvalidRefreshTokenException se il refresh token è invalido, scaduto
     *                                      o non corrisponde all'ID salvato
     */
    @Transactional
    public AuthResponse refresh(String accessToken, String refreshToken) {

        // verifico che i token non siano nulli o vuoti
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access Token non può essere nullo o vuoto.");
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh Token non può essere nullo o vuoto.");
        }

        // Controlla se l'access token è in blacklist
        if (tokenRevocatoService.isTokenPresent(accessToken)) {
            throw new InvalidAccessTokenException("Token di accesso in blacklist.");
        }

        // Recupero l'ID dell'utente attraverso la decodifica del token (anche se
        // scaduto)
        Long id = Long.valueOf(jwtService.extractIdAllowExpired(accessToken));

        // Ottengo l'utente corrispondente dal DB, se esiste. lancia eccezione
        // altrimenti
        CustomUserDetails dettagliUtente = userDetailsService.loadUserById(id);

        /*
         * Se possibile, eseguo cast da CustomUserDetails (interfaccia visibile a tutti
         * tramite spring security) a CustomPrincipal (oggetto effettivamente
         * implementante l'interfaccia, visibile solo dentro il modulo security), per
         * poter accedere all'ID del refresh token
         */
        if (!(dettagliUtente instanceof CustomPrincipal)) {
            throw new InvalidRefreshTokenException("Impossibile estrarre credenziali utente.");
        }
        CustomPrincipal utente = (CustomPrincipal) dettagliUtente;

        // In caso di invalidità o scadenza, in JwtService il metodo parseClaimsJws
        // lancia eccezione.
        String tid;
        try {
            tid = jwtService.extractTid(refreshToken);
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException("Refresh Token non valido o scaduto.", e);
        }

        // Controlla se refresh token inviato dall'utente corrisponde a quello salvato
        // in DB
        if (!utente.getTid().equals(tid)) {
            throw new InvalidRefreshTokenException("ID Refresh Token non corrisponde.");
        }

        // Genera nuovo access token
        String newAccessToken = jwtService.generateToken(utente);

        // verifico che il nuovo token per errore non sia stato già generato
        // e che sia valido
        if (tokenRevocatoService.isTokenPresent(newAccessToken)) {
            throw new InvalidAccessTokenException("Il token di accesso generato non è valido!");
        }

        // Nuovo ID del token refresh
        String newTid = UUID.randomUUID().toString();
        String newRefreshToken = jwtService.generateRefreshToken(utente, newTid);

        // Salva l'ID del nuovo refresh token nel DB
        authRepository.setTidById(newTid, utente.getId());

        // Salva l'access token vecchio in blacklist
        tokenRevocatoService.blackListToken(accessToken, jwtService.extractExpiration(accessToken).toInstant());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public String logout(String accessToken, String refreshToken) {

        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access Token non può essere nullo o vuoto.");
        }

        // Recupero id dall'access token
        Long id = Long.valueOf(jwtService.extractId(accessToken));

        // annullo il refresh token
        authRepository.setTidById(null, id);

        // metto l’access token in blacklist fino alla sua scadenza
        tokenRevocatoService.blackListToken(accessToken, jwtService.extractExpiration(accessToken).toInstant());

        return "Logout effettuato correttamente!";
    }

    public Map<String, String> register(RegisterRequest request) {

        // Controlla se l'email è già in uso
        if (authRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Utente già registrato con questa email");
        }

        // Aggiunta ruolo base
        HashSet<Ruolo> ruoli = new HashSet<>();
        ruoli.add(ruoloRepository.findByNome(RUOLO_BASE)
                .orElseThrow(
                        () -> new RuntimeException("Registrazione fallita: ruolo base per nuovi utenti non trovato")));

        // Creazione e salvataggio oggetto Credenziali per il nuovo utente
        Credenziali nuovoUtente = authRepository.save(Credenziali.builder()
                .email(request.getEmail())

                // Hashing password
                .password(passwordEncoder.encode(request.getPassword()))
                .ruoli(ruoli)
                .build());

        // Genera evento UtenteRegistrato per modulo anagrafica
        events.publishEvent(new UtenteRegistrato(
                nuovoUtente.getId(),
                request.getUsername(),
                nuovoUtente.getEmail()));

        // Restituisce un oggetto JSON con un messaggio
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registrazione effettuata con successo");

        // Invia conferma di registrazione
        return response;
    }
}

@StandardException
class InvalidRefreshTokenException extends RuntimeException {
}

@StandardException
class InvalidAccessTokenException extends RuntimeException {
}
