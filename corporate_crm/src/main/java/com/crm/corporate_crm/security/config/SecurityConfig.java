package com.crm.corporate_crm.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // Disabilita CSRF (dato che non usiamo cookie/sessione)
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Imposta la gestione delle sessioni come stateless (non viene usata la sessione HTTP)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Definisce le regole di accesso alle rotte
            .authorizeHttpRequests(auth -> auth
                // Le rotte di login e pubbliche non richiedono autenticazione
                .requestMatchers("/auth/**", "/public/**").permitAll()

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
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowCredentials(true);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // perché usi cookie HttpOnly per refresh

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
