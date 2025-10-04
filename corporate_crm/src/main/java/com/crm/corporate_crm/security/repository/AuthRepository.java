package com.crm.corporate_crm.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crm.corporate_crm.security.model.Credenziali;

@Repository
public interface AuthRepository extends JpaRepository<Credenziali, Long> {

  /** Metodo di ricerca per id del token refresh */
  Optional<Credenziali> findByTid(String tid);

  /** Ricerca per email (ignorando maiuscole-minuscole)*/
  @Query("select c from Credenziali c where lower(c.email) = lower(:email)")
  Optional<Credenziali> findByEmail(@Param("email") String email);

  /** Sovrascrittura id refresh token, in base a id utente */
  @Modifying
  @Query("update Credenziali c set c.tid = ?1 where c.id = ?2")
  int setTidById(String tid, Long id);
}