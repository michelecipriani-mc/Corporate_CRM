package com.crm.corporate_crm.anagrafica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtenteDto {
    //attibuti DTO
    private Long id; // Id Dto
    private String username; //username composto da nome e cognome
    private String email; //mail Dto
    private String password; //password Dto

}
