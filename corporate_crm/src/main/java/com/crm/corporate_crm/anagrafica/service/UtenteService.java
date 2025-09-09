package com.crm.corporate_crm.anagrafica.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.model.Utente;
import com.crm.corporate_crm.anagrafica.repository.UtenteRepository;
import com.crm.corporate_crm.security.api.dto.RegisterRequest;

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
    public UtenteDto findByEmail (String email) {
        return modelMapper.map(
                utenteRepository.findByEmail(email).orElseThrow(
                        () -> new RuntimeException("Email utente inesistente..")), UtenteDto.class);

    }

    public UtenteDto findByUsername(String username) {
        return modelMapper.map(
            utenteRepository.findByUsername(username).orElseThrow(
                    () -> new RuntimeException("Username inesistente..")), UtenteDto.class);
    }

  
    
    /** *Metodo per effettuare il salvataggio dell'utente */
    public UtenteInfoDto save (RegisterRequest dto) {
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
