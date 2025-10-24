package com.crm.corporate_crm.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.crm.corporate_crm.security.filter.JwtAuthFilter;
import com.crm.corporate_crm.security.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

/**
 * Configura la sicurezza dell'applicazione:
 * - Imposta il filtro JWT per le richieste
 * - Definisce le regole di autorizzazione
 * - Configura l'AuthenticationManager con UserDetailsService personalizzato
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Definisce la catena di sicurezza HTTP:
     * - Disabilita CSRF
     * - Imposta la sessione in modalità stateless (JWT)
     * - Applica regole di autorizzazione
     * - Registra il filtro JWT prima del filtro di autenticazione standard
     */
    /**
     * [MODIFICA 1]
     * Catena di sicurezza per i percorsi pubblici (autenticazione, registrazione, refresh).
     * Questa catena ha priorità più alta e NON applica il JwtAuthFilter.
     */
    @Bean
    @Order(1) // Priorità alta
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        return http
            // Applica questa catena solo ai percorsi specificati
            .securityMatcher("/auth/**", "/public/**")
            
            // Disabilita CSRF (dato che non usiamo cookie/sessione)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Imposta la gestione delle sessioni come stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Definisce le regole di accesso
            .authorizeHttpRequests(auth -> auth
                // Tutte le richieste che corrispondono a questo securityMatcher sono permesse
                .anyRequest().permitAll()
            )
            
            // !! NOTA: jwtFilter NON viene aggiunto qui !!
            
            .build();
    }

    /**
     * [MODIFICA 2]
     * Catena di sicurezza principale per tutti gli altri endpoint API.
     * Questa catena ha priorità più bassa e APPLICA il JwtAuthFilter.
     */
    @Bean
    @Order(2) // Priorità più bassa
    public SecurityFilterChain securedFilterChain(HttpSecurity http) throws Exception {
        return http
            // Applica questa catena a tutte le altre richieste
            .securityMatcher("/**") 
            
            // Disabilita CSRF (dato che non usiamo cookie/sessione)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Imposta la gestione delle sessioni come stateless (non viene usata la sessione HTTP)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Definisce le regole di accesso alle rotte
            .authorizeHttpRequests(auth -> auth
                // Le rotte /admin/** richiedono il ruolo ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Tutte le altre richieste devono essere autenticate
                .anyRequest().authenticated()
            )

            // Aggiunge il filtro JWT prima del filtro standard di autenticazione username/password
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // Costruisce e restituisce la catena
            .build();
    }

    /**
     * Configura l'AuthenticationManager da usare manualmente o nel controller.
     * Usa il servizio utenti personalizzato e il password encoder scelto (es. BCrypt).
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://localhost:4200")); // Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        //configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true); // perché usi cookie HttpOnly per refresh

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
