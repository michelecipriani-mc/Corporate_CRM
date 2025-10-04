package com.crm.corporate_crm.security.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.model.CustomPrincipal;
import com.crm.corporate_crm.security.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthRepository authRepository;
  private final ModelMapper modelMapper;

  public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {

    return modelMapper.map(
        authRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato")),
        CustomPrincipal.class);

    /*
     * return Credenziali.builder()
     * .id(id)
     * .password(u.getPassword())
     * .ruoliId(null)
     * .refreshToken(u.getRefreshToken())
     * .build();
     */
  }

  public UserDetails loadUserByEmail(String email) {
    return this.loadUserByUsername(email);
  }

  // Sostituzione username spring security con email
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    return modelMapper.map(
        authRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato")),
        CustomPrincipal.class);
  }
}
