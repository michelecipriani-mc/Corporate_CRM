package com.crm.corporate_crm.anagrafica.api.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

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

    //@NotBlank(message = "Il ruolo è obbligatorio (es: USER)")
    private String ruolo;

}