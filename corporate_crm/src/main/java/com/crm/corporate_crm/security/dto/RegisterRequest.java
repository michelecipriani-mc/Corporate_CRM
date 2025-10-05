package com.crm.corporate_crm.security.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @NotBlank(message = "Il campo nome completo è obbligatoria")
    private String username;

    @NotBlank(message = "Il campo email è obbligatoria")
    @Column(unique = true, nullable = false)
    @Email(message = "Inserire un indirizzo email valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
    message = "La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale")
    private String password;

    //@NotBlank(message = "Il ruolo è obbligatorio (es: USER)")
    private String ruolo;

}