package com.crm.corporate_crm.anagrafica.service;

import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.NuovoUtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.model.Utente;
import com.crm.corporate_crm.anagrafica.repository.UtenteRepository;
import com.crm.corporate_crm.anagrafica.api.service.UtenteServiceApi;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteService implements UtenteServiceApi {
    /** *Effettuo la DI della repository utente */
    private final UtenteRepository utenteRepository;

    private final ModelMapper modelMapper;

    /**
     * *Metodo di ricerca utente per id di registrazione, nel caso di utente
     * inesistente abbiamo una RuntimeException
     */
    public Optional<UtenteDto> findById(Long id) {
        return utenteRepository.findById(id)
                .map(utente -> modelMapper.map(utente, UtenteDto.class));
    }

    /** *Metodo di ricerca utente per email */
    public Optional<UtenteDto> findByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .map(utente -> modelMapper.map(utente, UtenteDto.class));
    }

    public UtenteDto findByUsername(String username) {
        return modelMapper.map(
                utenteRepository.findByUsername(username).orElseThrow(
                        () -> new RuntimeException("Username inesistente..")),
                UtenteDto.class);
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        Utente u = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email utente non trovata per aggiornamento token"));
        u.setRefreshToken(refreshToken);
        utenteRepository.save(u);
    }

    /** *Metodo per effettuare il salvataggio dell'utente */
    public UtenteInfoDto save(NuovoUtenteDto nuovoUtenteDto) {

        // Converti richiesta registrazione in entità utente
        Utente utente = modelMapper.map(nuovoUtenteDto, Utente.class);

        // Salva nuovo utente in DB
        Utente registrato = utenteRepository.save(utente);

        // Restituisci un UtenteInfoDTO per la response entity di conferma
        return modelMapper.map(registrato, UtenteInfoDto.class);
    }

    /**
     * *Metodo per l'eliminazione dell'utente attraverso il suo id di riferimento
     */
    public void delete(Long id) {
        utenteRepository.deleteById(id);
    }

    public Optional<UtenteInfoDto> getInfo(Long id) {
        return utenteRepository.findById(id)
                .map(utente -> modelMapper.map(utente, UtenteInfoDto.class));
    }

    public UtenteInfoDto update(Long id, UtenteInfoDto utenteInfoDto) {

        Utente utenteEsistente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));

        // Configura ModelMapper per aggiornare solo campi non nulli
        ModelMapper skipNullModelMapper = new ModelMapper();
        skipNullModelMapper.getConfiguration()
                .setSkipNullEnabled(true) // evita di sovrascrivere i campi con null
                .setPropertyCondition(Conditions.isNotNull()); // condizione globale

        // Applica il mapping (solo i campi non nulli vengono copiati)
        skipNullModelMapper.map(utenteInfoDto, utenteEsistente);

        // Salva l'entità aggiornata nel database
        Utente aggiornato = utenteRepository.save(utenteEsistente);

        // Converte l'entità aggiornata in DTO per la response
        return modelMapper.map(aggiornato, UtenteInfoDto.class);
    }

}
