package com.crm.corporate_crm.anagrafica.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
    private Long id; //attributo ID

    private String nomeRuolo; //attributo nome ruolo

    @ManyToMany
    private List<Utente> utenti;

    @ElementCollection
    private Set<Long> richiestaCariera_id; //lista delle richieste di cariera legate al ruolo

}
