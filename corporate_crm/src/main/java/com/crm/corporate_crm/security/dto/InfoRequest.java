package com.crm.corporate_crm.security.dto;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoRequest {
    @NotBlank(message = "Il campo cellulare è obbligatoria")
    @Column(unique = true, nullable = false)
    @Pattern(
        regexp = "\"3\\\\d{8,9}\"", 
        message = "Inserire un numero di cellulare valido")
    private String cellulare; // numero di telefono

    @NotBlank(message = "Il campo Codice Fiscale è obbligatoria")
    @Column(unique = true, nullable = false)
    @Pattern(
        regexp = "[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]", 
        message = "Inserire un Codice Fiscale valido")
    private String codiceFiscale; // codice fiscale

    @NotBlank(message = "Il campo Indirizzo è obbligatoria")
    private String indirizzo; // indirizzo di residenza

    @NotBlank(message = "Il campo Città è obbligatoria")
    private String citta; // città di residenza

    @NotBlank(message = "Il campo Provincia è obbligatoria")
    @Pattern(
        regexp = "[A-Z]{2}", 
        message = "Inserire una Provincia valida di massimo 2 lettere")
    private String provincia; // provincia di residenza

    @NotBlank(message = "Il campo CAP è obbligatoria")
    @Pattern(
        regexp = "\\d{5}", 
        message = "Inserire un CAP valido di massimo 5 cifre")
    private String cap; // cap

    @NotBlank(message = "Il campo IBAN è obbligatoria")
    @Column(unique = true, nullable = false)
    @Pattern(
        regexp = "[A-Z]{2}\\d{2}[0-9A-Z]{23}", 
        message = "Inserire un IBAN valido")
    private String iban; // iban

    @NotBlank(message = "Il campo data di nascita è obbligatorio")
    private Date dataNascita;
}
