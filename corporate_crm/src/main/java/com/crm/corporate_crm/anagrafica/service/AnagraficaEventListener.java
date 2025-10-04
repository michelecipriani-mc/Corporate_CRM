package com.crm.corporate_crm.anagrafica.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.crm.corporate_crm.common.events.UtenteRegistrato;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnagraficaEventListener {

  private final UtenteService utenteService;

  // Ascolta l'evento pubblicato dal modulo Security
  @EventListener
  public void handleNewUserRegistration(UtenteRegistrato event) {
    System.out.println("Anagrafica received event for user: " + event.getUsername());

    // Chiama il servizio di Anagrafica per aggiornare la rubrica
    utenteService.save(event);
  }
}
