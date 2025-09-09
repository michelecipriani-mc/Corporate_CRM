package com.crm.corporate_crm.security.api.dto;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @NotBlank(message = "Il campo username è obbligatorio")
    @Size(min = 3, max = 25, message = "Lo username deve avere tra 3 e 25 caratteri")
    private String username;

    @NotBlank(message = "Il campo email è obbligatoria")
    @Column(unique = true, nullable = false)
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$", 
        message = "Inserire un indirizzo email valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
    message = "La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale")
    private String password;

    @NotBlank(message = "Il ruolo è obbligatorio (es: USER)")
    private String ruolo;

}