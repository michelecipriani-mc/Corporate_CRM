package com.crm.corporate_crm.anagrafica.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.model.Utente;
import com.crm.corporate_crm.anagrafica.repository.UtenteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteService {
    /** *Effettuo la DI della repository utente */
    private final UtenteRepository utenteRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    /** *Metodo di ricerca utente per id di registrazione, nel caso di utente inesistente abbiamo una RuntimeException */
    public Utente findById (Long id) {
        return utenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato o inesistente.."));
    }

    /** *Metodo di ricerca utente per email */
    public Utente findByEmail (String email) {
        return utenteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email utente inesistente.."));
    }
    
    /** *Metodo per effettuare il salvataggio dell'utente */
    public UtenteInfoDto save (UtenteDto dto) {
        Utente utente = modelMapper.map(dto, Utente.class);
        utente.setPassword(passwordEncoder.encode(dto.getPassword()));
        Utente registrato = utenteRepository.save(utente);
        return modelMapper.map(registrato, UtenteInfoDto.class);
    }

    /** *Metodo per l'eliminazione dell'utente attraverso il suo id di riferimento */
    public void delete (Long id) {
        utenteRepository.deleteById(id);
    }

}
