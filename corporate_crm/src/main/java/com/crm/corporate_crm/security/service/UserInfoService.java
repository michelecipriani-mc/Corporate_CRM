package com.crm.corporate_crm.security.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService {

  private final UtenteServiceApi utenteServiceApi;



  public Optional<UtenteInfoDto> getUserInfo(CustomUserDetails principal, Long id) {

    // Se l'utente chiede informazioni di se stesso,
    // Si possono saltare i filtri
    if (!principal.getId().equals(id)) {
      
  
      // È necessario filtrare le informazioni

      // ...............

    }

    return utenteServiceApi.getInfo(id);
  }



  public Optional<UtenteInfoDto> getPersonalInfo(CustomUserDetails principal) {
    
    return utenteServiceApi.getInfo(principal.getId());
  }
}
