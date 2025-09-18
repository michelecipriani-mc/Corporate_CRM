import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit{
  userEmail: string = '';
  auth: any;

  constructor() { }

  ngOnInit(): void {
    // Qui potresti recuperare i dati dell'utente dal localStorage
    // o da un servizio di stato per mostrare un messaggio personalizzato
    // Esempio:
    this.userEmail = this.auth.getCurrentUserEmail();
  }
}
