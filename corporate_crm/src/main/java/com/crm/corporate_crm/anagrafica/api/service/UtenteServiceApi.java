package com.crm.corporate_crm.anagrafica.api.service;

import java.util.Optional;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.security.api.dto.RegisterRequest;

public interface UtenteServiceApi {
  /** Ricerca per id */
  Optional<UtenteDto> findById(Long id);

  /** Ricerca per email */
  Optional<UtenteDto> findByEmail(String email);

  /** Ricerca per Username */
  UtenteDto findByUsername(String username);

  /** metodo per aggiornare e salvare il refresh token */
  void updateRefreshToken(Long Id, String refreshToken);

  /** Registra utente */
  UtenteInfoDto save(RegisterRequest registerUserDto);
}
