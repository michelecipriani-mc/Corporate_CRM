import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Per ngModel
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  user = {
    email: '',
    password: ''
  };
  errorMessage: string = '';

  constructor(private authService: Auth, private router: Router) { }

  onRegister(): void {
    this.authService.register(this.user).subscribe({
      next: (response) => {
        console.log('Registrazione completata', response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Errore durante la registrazione', error);
        this.errorMessage = 'Registrazione fallita. Riprova.';
      }
    });
  }
}
