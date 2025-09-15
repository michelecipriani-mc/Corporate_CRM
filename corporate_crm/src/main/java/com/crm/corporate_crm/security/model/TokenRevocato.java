package com.crm.corporate_crm.security.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class TokenRevocato {
    // attributi della classe    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //attributo id

    private String token; //attributo token

    private Instant expiryDate; // attributo per la data di cessazione validità

    //costruttore specifico
    public TokenRevocato (String token, Instant expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

}
