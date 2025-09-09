package com.crm.corporate_crm.anagrafica.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crm.corporate_crm.anagrafica.dto.UtenteDto;
import com.crm.corporate_crm.anagrafica.dto.UtenteInfoDto;
import com.crm.corporate_crm.anagrafica.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/utenti")
@RequiredArgsConstructor
public class UtenteController {
    
    private final UtenteService utenteService;

    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public UtenteInfoDto getById(@PathVariable Long id) {
        return modelMapper.map(utenteService.findById(id), UtenteInfoDto.class);
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteInfoDto> register (@Validated @RequestBody UtenteDto dto) {
        UtenteInfoDto utente = utenteService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(utente);
    }
    
}
