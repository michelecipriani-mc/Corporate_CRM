package com.crm.corporate_crm.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtenteRegistrato {
    Long id;
    String username;
    String email;
}

