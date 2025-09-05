package com.crm.corporate_crm.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Il campo username è obbligatorio")
    @Size(min = 4, max = 20, message = "Lo username deve avere tra 4 e 20 caratteri")
    private String username;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Il ruolo è obbligatorio (es: USER)")
    private String ruolo;

}