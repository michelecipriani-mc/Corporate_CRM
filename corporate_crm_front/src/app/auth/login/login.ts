import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  credentials = {
    email: '',
    password: ''
  };
  errorMessage: string = '';

  constructor(private authService: Auth, private router: Router) { }

  onLogin(): void {
    this.authService.login(this.credentials).subscribe({
      next: () => {
        console.log('Login completato');
        // Reindirizza alla pagina principale dopo il login
        this.router.navigate(['/']); 
      },
      error: (error) => {
        console.error('Errore durante il login', error);
        this.errorMessage = 'Credenziali non valide.';
      }
    });
  }
}
