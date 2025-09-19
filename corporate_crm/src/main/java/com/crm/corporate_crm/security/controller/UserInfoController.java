package com.crm.corporate_crm.security.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crm.corporate_crm.anagrafica.api.dto.UtenteInfoDto;
import com.crm.corporate_crm.security.api.dto.CustomUserDetails;
import com.crm.corporate_crm.security.service.UserInfoService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST per la gestione dell'autenticazione tramite JWT.
 * Espone un endpoint POST /auth/login per autenticare un utente
 * e restituire un token JWT valido.
 */
@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
@Validated
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/you")
    public ResponseEntity<UtenteInfoDto> getPersonalInfo(
            @AuthenticationPrincipal CustomUserDetails principal,
            HttpServletResponse response
        ) {
        return ResponseEntity.ok(
                    userInfoService.getPersonalInfo(principal)
                            .orElseThrow(() -> new RuntimeException("Informazioni personali non visibili")));
    }

}
