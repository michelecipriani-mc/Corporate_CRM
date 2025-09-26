package com.crm.corporate_crm.anagrafica.api.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtenteDto {
    //attibuti DTO
    private Long id; // Id Dto
    private String email; //mail Dto
    private String username; //username Dto
    private String password; //password Dto
    private Set<Long> ruoliId; //lista dei ruoli associati all'utenete
    private String refreshToken; // attributo per refreshare il Token
}
