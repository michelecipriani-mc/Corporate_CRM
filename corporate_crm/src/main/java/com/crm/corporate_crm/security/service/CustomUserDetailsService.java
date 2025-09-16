package com.crm.corporate_crm.security.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.model.CustomPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteServiceApi utenteServiceApi;
    private final ModelMapper modelMapper;

    public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {

    return modelMapper.map(
            utenteServiceApi.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato")),
            CustomPrincipal.class);

        /*return UtenteDto.builder()
                .id(id)
                .password(u.getPassword())
                .ruoliId(null)
                .refreshToken(u.getRefreshToken())
                .build();*/
    }

    public UserDetails loadUserByEmail(String email) {
        return this.loadUserByUsername(email);
    }

    // Sostituzione username spring security con email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return modelMapper.map(
                utenteServiceApi.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato")),
                CustomPrincipal.class);

        /*
         * return UtenteDto.builder()
         * .id(u.getId())
         * .email(email)
         * .password(u.getPassword())
         * .ruoliId(null)
         * .refreshToken(u.getRefreshToken())
         * .build();
         */

        /*
         * return User.builder()
         * .username(u.getEmail())
         * .password(u.getPassword())
         * .roles(u.getRuoli()
         * .stream()
         * .map(Ruolo::getNome)
         * .toArray(String[]::new)) // es: "USER"
         * .build();
         */
    }
}
