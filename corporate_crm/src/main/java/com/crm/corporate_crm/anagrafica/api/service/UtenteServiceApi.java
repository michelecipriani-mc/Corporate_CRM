package com.crm.corporate_crm.anagrafica.api.service;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.security.api.dto.RegisterRequest;

public interface UtenteServiceApi {
  UtenteDto findByEmail(String email);

  void updateRefreshToken(String username, String refreshToken);

  /** Registra utente */
  void save(RegisterRequest registerUserDto);
}
