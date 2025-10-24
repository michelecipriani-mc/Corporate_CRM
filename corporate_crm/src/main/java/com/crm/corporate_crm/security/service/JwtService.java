package com.crm.corporate_crm.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.security.api.dto.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service // Indica che questa classe è un componente di servizio gestito da Spring
public class JwtService {

    // Recupera la chiave segreta per firmare/verificare i token JWT dal file
    // application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Recupera la durata del token (in millisecondi) dal file
    // application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    // Recupera la durata del token (in millisecondi) dal file
    // application.properties
    @Value("${jwt.refreshexpiration}")
    private long refreshExpiration;

    /**
     * Genera un token JWT per l'utente autenticato.
     * - Inserisce lo username come subject
     * - Inserisce i ruoli come claim
     * - Firma il token con algoritmo HS256 usando la chiave segreta
     */
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // Username come subject
                .claim("id", ((CustomUserDetails) user).getId())
                .claim("roles", user.getAuthorities()) // Ruoli dell’utente come claim personalizzato
                .setIssuedAt(new Date()) // Data di emissione
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Data di scadenza
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256) // Firma del token
                .compact(); // Compatta il tutto in una stringa JWT
    }

    public String generateRefreshToken(CustomUserDetails user, String tid) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("tid", tid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration)) // es. 7 gg
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Estrae lo username (subject) da un token JWT.
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Estrae lo username (subject) da un token JWT, senza lanciare eccezioni se è
     * scaduto. 
     * Utile per il refresh.
     */
    public String extractUsernameAllowExpired(String token) {
        return getClaimsAllowExpired(token).getSubject();
    }

    /**
     * Verifica se un token è valido per un determinato utente.
     * - Lo username del token deve corrispondere a quello dell'utente
     * - Il token non deve essere scaduto
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    /**
     * Estrae tutti i claims (contenuto) da un token JWT.
     * - Serve per leggere subject, expiration, claims personalizzati, ecc.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes()) // Imposta la chiave segreta per la verifica
                .build()
                .parseClaimsJws(token) // Parsea e verifica il token firmato
                .getBody(); // Restituisce il contenuto (claims)
    }

    /**
     * Estrae i claims da un token JWT, a prescindere dal fatto che sia scaduto o
     * meno.
     * Utile per il refresh.
     */
    private Claims getClaimsAllowExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            // Token scaduto, ma firma valida
            return ex.getClaims();
        }
    }

    /**
     * Verifica se un token JWT è scaduto.
     */
    private boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Recupera la data dell'avvenuta scadenza del token.
     */
    public Date extractExpiration(String accessToken) {
        return getClaims(accessToken).getExpiration();
    }

    public String extractId(String accessToken) {
        return getClaims(accessToken).getId();
    }

}
