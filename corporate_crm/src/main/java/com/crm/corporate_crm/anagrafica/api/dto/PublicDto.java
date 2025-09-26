package com.crm.corporate_crm.anagrafica.api.dto;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicDto {

    //attibuti DTO
    private Long id; // Id Dto
    private String username; //username composto da nome e cognome
    private String email; //mail Dto
    private String cellulare; //Dto numero di telefono
    private String città; //Dto città di residenza
    private String provincia; //Dto provincia di residenza
    private Set<Long> ruoli = new HashSet<>(); //ruoli associati all'utente
    private Set<Long> commesse_id = new HashSet<>(); //lista delle commesse abilitate per utente
    
}
