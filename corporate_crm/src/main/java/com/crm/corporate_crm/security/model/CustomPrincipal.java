package com.crm.corporate_crm.security.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.crm.corporate_crm.security.api.dto.CustomUserDetails;

import com.crm.corporate_crm.security.model.TipiRuolo;

import lombok.Data;

@Data
public class CustomPrincipal implements CustomUserDetails {

    private Long id; // Id 
    private String email; //mail 
    private String username; //username 
    private String password; //password 
    private Set<TipiRuolo> ruoliId = new HashSet<>(); //lista dei ruoli associati all'utente

    /** ID del refresh token */
    private String tid;


    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // throw new UnsupportedOperationException("Unimplemented method
        // 'getAuthorities'");
        return Collections.emptyList();
    }

}
