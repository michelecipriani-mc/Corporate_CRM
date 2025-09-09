package com.crm.corporate_crm.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Il campo username è obbligatorio")
    @Size(min = 3, max = 25, message = "Lo username deve avere tra 3 e 25 caratteri")
    private String username;

    @NotBlank(message = "La password è obbligatoria")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
    message = "La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale")
    private String password;

    @NotBlank(message = "Il ruolo è obbligatorio (es: USER)")
    private String ruolo;

}