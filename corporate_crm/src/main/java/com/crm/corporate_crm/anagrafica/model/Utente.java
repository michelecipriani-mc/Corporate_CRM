package com.crm.corporate_crm.anagrafica.model;

import java.util.HashSet;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utente")
public class Utente {
    //attributi della classe

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //attributo ID

    private String username; //attributo composto da nome e cognome

    private String cellulare; //attributo numero di telefono

    private String codiceFiscale; //attributo codice fiscale

    private String indirizzo; //attributo indirizzo di residenza

    private String città; //attributo città di residenza

    private String provincia; //attributo provincia di residenza

    private String cap; //attributo cap

    private String iban; //attributo iban

    private String email; //attributo email

    private String password; //attributo password 

    private String refreshToken; //attributo per refreshare il Token

    @ManyToMany //vincolo Molti a Molti con l'entità ruoli
    //Riferimenti per la creazione della tabella N-M
    @JoinTable(name = "utenti_ruoli",
        joinColumns = {@JoinColumn(name = "UTENTE_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "RUOLO_ID", referencedColumnName = "ID")})
    private Set<Ruolo> ruoli = new HashSet<>(); //lista dei ruoli associati all'utente

    @ElementCollection
    private Set<Long> annunci_id; //lista degli annunci a cui l'utente si è candidato

    @ElementCollection
    private Set<Long> commesse_id; //lista delle commesse abilitate per utente

    @ElementCollection
    private Set<Long> documenti_id; //lista dei documenti collegati all'utente

}