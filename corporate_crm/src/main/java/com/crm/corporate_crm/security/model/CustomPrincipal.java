package com.crm.corporate_crm.security.model;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteDto;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;

public class CustomPrincipal extends UtenteDto implements CustomUserDetails {}
