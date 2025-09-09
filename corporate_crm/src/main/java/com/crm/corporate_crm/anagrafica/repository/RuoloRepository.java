package com.crm.corporate_crm.anagrafica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.corporate_crm.anagrafica.model.Ruolo;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {

    Optional<Ruolo> findByNome(String ruolo);
    
}
