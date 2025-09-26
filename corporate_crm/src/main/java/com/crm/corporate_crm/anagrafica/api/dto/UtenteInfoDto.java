package com.crm.corporate_crm.anagrafica.api.dto;

import java.sql.Date;
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
    private Date dataNascita;
    private String email; //mail Dto
    private String cellulare; //Dto numero di telefono
    private String codiceFiscale; //Dto codice fiscale
    private String indirizzo; //Dto indirizzo di residenza
    private String citta; //Dto città di residenza
    private String provincia; //Dto provincia di residenza
    private String cap; //Dto cap
    private String iban; //Dto iban
    private Set<Ruolo> ruoli = new HashSet<>(); //ruoli associati all'utente
    private Set<Long> commesse_id = new HashSet<>(); //lista delle commesse abilitate per utente
    private Set<Long> documenti_id = new HashSet<>(); //lista dei documenti collegati all'utente
    
}
