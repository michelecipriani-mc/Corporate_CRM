import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {

  /**
   * Controlla la visibilità del menu a discesa dell'utente.
   */
  public isDropdownOpen: boolean = false;

  constructor() { }

  /**
   * Alterna lo stato della variabile, forzando Angular a mostrare/nascondere il menu.
   */
  toggleDropdown(): void {
    console.log('Toggle click! Stato precedente:', this.isDropdownOpen);
    this.isDropdownOpen = !this.isDropdownOpen;
    console.log('Stato attuale:', this.isDropdownOpen);
  }
}
