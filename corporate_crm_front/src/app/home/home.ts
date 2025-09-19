import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Auth } from '../services/auth';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private apiUrl = `${environment.apiUrl}/auth`;
  userEmail: null | string = '';
  userData: any;

  constructor(private authService: Auth, private http: HttpClient) {}

  ngOnInit(): void {
    // Qui potresti recuperare i dati dell'utente dal localStorage
    // o da un servizio di stato per mostrare un messaggio personalizzato
    // Esempio:
    this.userEmail = this.authService.getToken();

    this.authService.getUserInfo().subscribe({
      next: (data) => {
        this.userData = data;
      },
      error: (err) => {
        console.error('Errore nel recupero dei dati utente', err);
      },
    });

  }
}
