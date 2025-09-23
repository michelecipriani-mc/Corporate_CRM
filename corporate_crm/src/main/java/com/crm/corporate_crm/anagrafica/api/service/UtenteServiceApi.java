package com.crm.corporate_crm.anagrafica.api.service;

import java.util.Optional;

import com.crm.corporate_crm.anagrafica.api.dto.NuovoUtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;

public interface UtenteServiceApi {
  /** Ricerca per id */
  Optional<UtenteDto> findById(Long id);

  /** Ricerca per email */
  Optional<UtenteDto> findByEmail(String email);

  /** Ricerca per Username */
  UtenteDto findByUsername(String username);

  /** metodo per aggiornare e salvare il refresh token */
  void updateRefreshToken(Long id, String refreshToken);

  /** Registra utente */
  UtenteInfoDto save(NuovoUtenteDto nuovoUtenteDto);

  /** Ottieni informazioni */
  Optional<UtenteInfoDto> getInfo(Long id);

  UtenteInfoDto update(Long id, UtenteInfoDto utenteInfoDto);

}
