package com.crm.corporate_crm.anagrafica.api.service;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.security.api.dto.RegisterRequest;

public interface UtenteServiceApi {
  UtenteDto findByEmail(String email);

  UtenteDto findByUsername(String username);
  void updateRefreshToken(String username, String refreshToken);

  /** Registra utente */
  UtenteInfoDto save(RegisterRequest registerUserDto);
}
