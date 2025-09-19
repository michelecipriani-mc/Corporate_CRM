package com.crm.corporate_crm.anagrafica.api.dto;

import java.util.HashSet;
import java.util.Set;

import com.crm.corporate_crm.anagrafica.model.Ruolo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtenteInfoDto {

    //attibuti DTO
    private Long id; // Id Dto
    private String username; //username composto da nome e cognome
    private String email; //mail Dto


    private Set<Ruolo> ruoli = new HashSet<>(); //ruoli associati all'utente

    private Set<Long> commesse_id = new HashSet<>(); //lista delle commesse abilitate per utente
    private Set<Long> documenti_id = new HashSet<>(); //lista dei documenti collegati all'utente
    
}
