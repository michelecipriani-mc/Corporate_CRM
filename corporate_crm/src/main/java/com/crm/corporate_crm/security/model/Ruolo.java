package com.crm.corporate_crm.security.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ruolo")
public class Ruolo {
    //lista attributi
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    private String nome; // nome ruolo

    private List<Long> utenti;

    @ElementCollection
    private Set<Long> richiestaCarrieraId; //lista delle richieste di carriera legate al ruolo

}
