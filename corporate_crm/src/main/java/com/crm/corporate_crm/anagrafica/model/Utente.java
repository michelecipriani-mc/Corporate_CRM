package com.crm.corporate_crm.anagrafica.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utente")
public class Utente {
    //attributi della classe

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //attributo ID

    private String nome; //attributo nome 

    private String cognome; //attributo cognome 

    private String email; //attributo email

    private String password; //attributo password 

    @ManyToMany //vincolo Molti a Molti con l'entità ruoli
    //Riferimenti per la creazione della tabella N-M
    @JoinTable(name = "utenti_ruoli",
        joinColumns = {@JoinColumn(name = "UTENTE_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "RUOLO_ID", referencedColumnName = "ID")})
    private List<Ruolo> ruoli; //lista dei ruoli associati all'utenete

    @ElementCollection
    private Set<Long> annunci_id; //lista degli annunci a cui l'utente si è candidato

    @ElementCollection
    private Set<Long> commesse_id; //lista delle commesse abilitate per utente

    @ElementCollection
    private Set<Long> documenti_id; //lista dei documenti collegati all'utente

}