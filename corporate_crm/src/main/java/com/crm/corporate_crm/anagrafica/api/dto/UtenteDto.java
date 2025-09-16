package com.crm.corporate_crm.anagrafica.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.crm.corporate_crm.anagrafica.model.TipiRuolo;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;

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
    private Set<TipiRuolo> ruoliId = new HashSet<>(); //lista dei ruoli associati all'utenete
    private String refreshToken; // attributo per refreshare il Token

    //@Override
    public String getUsername() {
        return this.email;
    }

    //@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
        return Collections.emptyList();
    }

}
