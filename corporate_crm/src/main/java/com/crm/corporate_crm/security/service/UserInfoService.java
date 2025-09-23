package com.crm.corporate_crm.security.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.dto.InfoRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService {

  private final UtenteServiceApi utenteServiceApi;
  private final ModelMapper modelMapper;



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

  public void setInfoAggiuntive(CustomUserDetails principal, InfoRequest infoRequest) {

    utenteServiceApi.update(principal.getId(), modelMapper.map(infoRequest, UtenteInfoDto.class));

  }

}
