package com.crm.corporate_crm.anagrafica.service;

import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
        // 1. Recupera l'utente esistente
        Utente utenteEsistente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // 2. ModelMapper con TypeMap locale per aggiornare solo campi non nulli
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<UtenteInfoDto, Utente> typeMap = modelMapper.createTypeMap(UtenteInfoDto.class, Utente.class);
        typeMap.addMappings(mapper -> mapper.when(Conditions.isNotNull()));

        // 3. Mappa il DTO sull'entità esistente
        typeMap.map(utenteInfoDto, utenteEsistente);
        utenteEsistente.setId(id);

        // 4. Salva l'entità aggiornata
        Utente aggiornato = utenteRepository.save(utenteEsistente);

        // 5. Restituisci un DTO per la response
        return modelMapper.map(aggiornato, UtenteInfoDto.class);
    }

}
