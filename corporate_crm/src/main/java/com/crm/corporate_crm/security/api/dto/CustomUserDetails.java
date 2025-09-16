package com.crm.corporate_crm.security.api.dto;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails{
    Long getId();

    String getEmail();

    String getRefreshToken();

    
}
