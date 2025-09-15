package com.crm.corporate_crm.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.crm.corporate_crm.security.model.TokenRevocato;

@Repository
public interface TokenRevocatoRepository extends JpaRepository<TokenRevocato, Long> {
    /** Metodo di ricerca per Token */
    Optional<TokenRevocato> findByToken(String token);
} 