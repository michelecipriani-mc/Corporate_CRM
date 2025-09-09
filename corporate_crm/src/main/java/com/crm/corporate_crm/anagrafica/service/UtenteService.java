package com.crm.corporate_crm.anagrafica.service;

import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.model.Ruolo;
import com.crm.corporate_crm.anagrafica.model.Utente;
import com.crm.corporate_crm.anagrafica.repository.RuoloRepository;
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

    private final RuoloRepository ruoloRepository;

    /** *Metodo di ricerca utente per id di registrazione, nel caso di utente inesistente abbiamo una RuntimeException */
    public Utente findById (Long id) {
        return utenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato o inesistente.."));
    }

    /** *Metodo di ricerca utente per email */
    public Optional<UtenteDto> findByEmail (String email) {

        return utenteRepository.findByEmail(email)
                .map(utente -> modelMapper.map(utente, UtenteDto.class));
    }

    public UtenteDto findByUsername(String username) {
        return modelMapper.map(
            utenteRepository.findByUsername(username).orElseThrow(
                    () -> new RuntimeException("Username inesistente..")), UtenteDto.class);
    }

  
    
    /** *Metodo per effettuare il salvataggio dell'utente */
    public UtenteInfoDto save (RegisterRequest dto) {
    
        // Converti richiesta registrazione in entità utente
        Utente utente = modelMapper.map(dto, Utente.class);

        // Hashing password
        utente.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Inserisci ruoli base
        Set<Ruolo> ruoliIniziali = Set.of();
        ruoliIniziali.add(
                ruoloRepository.findByNome("UTENTE")
                    .orElseThrow(() -> new RuntimeException("Ruolo base per nuovi utenti assente.")));
        
        utente.setRuoli(ruoliIniziali);

        // Salva nuovo utente in DB
        Utente registrato = utenteRepository.save(utente);

        // Restituisci un UtenteInfoDTO per la response entity di conferma
        return modelMapper.map(registrato, UtenteInfoDto.class);
    }

    /** *Metodo per l'eliminazione dell'utente attraverso il suo id di riferimento */
    public void delete (Long id) {
        utenteRepository.deleteById(id);
    }

}
