package com.crm.corporate_crm.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.service.CustomUserDetailsService;
import com.crm.corporate_crm.security.service.JwtService;
import com.crm.corporate_crm.security.service.TokenRevocatoService;

import java.io.IOException;

/**
 * Filtro che intercetta ogni richiesta HTTP e verifica la presenza di un token JWT valido.
 * Se il token è presente e valido, autentica l’utente impostando lo UserDetails nel SecurityContext.
 */
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenRevocatoService tokenRevocatoService;

    /**
     * Metodo eseguito automaticamente ad ogni richiesta HTTP.
     * Controlla se c'è un header Authorization con un token JWT valido.
     * Se sì, autentica l'utente associato e imposta il contesto di sicurezza.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        // Estrae l'header Authorization dalla richiesta
        String authHeader = request.getHeader("Authorization");

        // Se l'header è nullo o non inizia con "Bearer ", salta il filtro e prosegue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrae il token JWT eliminando il prefisso "Bearer "
        String token = authHeader.substring(7).trim();

        // Header vuoto
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Estrae l'ID dal token
        Long id = Long.valueOf(jwtService.extractId(token));

        // Se l'ID è presente e l'utente non è già autenticato
        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carica l'utente dal database (o da un servizio)
            CustomUserDetails user = userDetailsService.loadUserById(id);

            // Verifica se il token è valido per l'utente caricato, e se non è in blacklist
            if (jwtService.isTokenValid(token, user) && !tokenRevocatoService.isTokenPresent(token)) {

                // Crea un oggetto di autenticazione con ruoli e dettagli dell’utente
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
                );

                // Aggiunge ulteriori dettagli della richiesta (IP, sessione, ecc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Imposta l'utente autenticato nel SecurityContext di Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua con la catena dei filtri
        filterChain.doFilter(request, response);
    }
}