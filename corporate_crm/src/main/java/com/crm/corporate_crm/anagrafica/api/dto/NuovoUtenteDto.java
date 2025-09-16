package com.crm.corporate_crm.anagrafica.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NuovoUtenteDto {

    private String username;
    private String email;
    private String password;
    private String ruoloIniziale;

}
