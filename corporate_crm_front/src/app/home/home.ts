import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
// import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Auth } from '../services/auth';
import { environment } from '../../environments/environment';
import { HomeModal } from "../home-modal/home-modal";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HomeModal],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private apiUrl = `${environment.apiUrl}/info`;
  userEmail: null | string = '';
  userData: any;
  showWidget = false;
  campiMancanti: string[] = [];

  constructor(private authService: Auth, private http: HttpClient) {}

  ngOnInit(): void {
    // Qui potresti recuperare i dati dell'utente dal localStorage
    // o da un servizio di stato per mostrare un messaggio personalizzato
    // Esempio:
    this.userEmail = this.authService.getToken();

    this.authService.getUserInfo().subscribe({
      next: (data) => {
        this.userData = data;
        this.controllaCampiMancanti(this.userData);
      },
      error: (err) => {
        console.error('Errore nel recupero dei dati utente', err);
      },
    });
  }

  controllaCampiMancanti(user: any) {
    const campiDaControllare = [
      'cellulare',
      'dataNascita',
      'indirizzo',
      'citta',
      'provincia',
      'cap',
      'codiceFiscale',
      'iban'
    ];
    this.campiMancanti = campiDaControllare.filter(campo => !user[campo]);
    this.showWidget = this.campiMancanti.length > 0;
  }

  onDatiNonCompilati(campi: string[]) {
    this.campiMancanti = campi;
    this.showWidget = campi.length > 0;
  }

  onDatiCompilati(dati: any) {
    console.log("Dati compilati dal form:", dati);

    // aggiorna localmente
    this.userData = { ...this.userData, ...dati };

    // richiama check per nascondere la modale se completato
    this.controllaCampiMancanti(this.userData);

    // opzionale: invio al backend
    this.http.put(`${this.apiUrl}/edit`, this.userData).subscribe({
      next: (res) => console.log("Utente aggiornato", res),
      error: (err) => console.error("Errore aggiornamento utente", err),
    });
  }

  apriWidget() {
    this.showWidget = true;
  }

  onChiudiWidget() {
    this.showWidget = false;
  }

}
