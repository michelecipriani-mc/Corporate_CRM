package com.crm.corporate_crm.security.service;

import java.time.Instant;
import org.springframework.stereotype.Service;
import com.crm.corporate_crm.security.model.TokenRevocato;
import com.crm.corporate_crm.security.repository.TokenRevocatoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenRevocatoService {
    /** DI della repository del token revocato */
    private TokenRevocatoRepository tokenRevocatoRepository;

    /** Metodo per la regiostrazione del token revocato in blacklist */
    public void blackListToken (String token, Instant expiryDate) {
        TokenRevocato tokenRevocato = new TokenRevocato(token, expiryDate);
        tokenRevocatoRepository.save(tokenRevocato);
    }

    /** Metodo per la verifica della presenza del token in blacklist */
    public boolean isPresentToken (String token) {
        return tokenRevocatoRepository.findByToken(token).isPresent();
    }

}
