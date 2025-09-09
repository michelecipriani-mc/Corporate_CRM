package com.crm.corporate_crm.anagrafica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.corporate_crm.anagrafica.model.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email); //ricerca utente per email

    Optional<Utente> findByUsername(String username); //ricerca utente per username
    
}
