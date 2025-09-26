package com.crm.corporate_crm.security.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Credenziali {
      
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @ManyToMany //vincolo Molti a Molti con l'entità ruoli
    //Riferimenti per la creazione della tabella N-M
    @JoinTable(name = "utenti_ruoli",
        joinColumns = {@JoinColumn(name = "UTENTE_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "RUOLO_ID", referencedColumnName = "ID")})
    private Set<Ruolo> ruoli = new HashSet<>(); //lista dei ruoli associati all'utente

    /** ID del refresh token */
    private String tid;

}
