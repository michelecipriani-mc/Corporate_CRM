package com.crm.corporate_crm.anagrafica.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.model.Ruolo;
import com.crm.corporate_crm.anagrafica.model.Utente;
import com.crm.corporate_crm.anagrafica.repository.UtenteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    // Sostituzione username spring security con email

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utente u = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        return User.builder()
                .username(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRuoli()
                        .stream()
                        .map(Ruolo::getNome)
                        .toArray(String[]::new)) // es: "USER"
                .build();
    }
}
