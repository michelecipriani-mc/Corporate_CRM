import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar {

    /**
   * Variabile di stato che controlla se la sidebar è espansa o meno.
   * Viene usata dal template HTML tramite [ngClass]="{ 'expanded': isExpanded }".
   */
  public isExpanded: boolean = false;

  constructor() { }

  /**
   * Imposta isExpanded a true quando il mouse entra nell'area della sidebar.
   * Associata a (mouseenter) nel template.
   */
  onMouseEnter(): void {
    this.isExpanded = true;
  }

  /**
   * Imposta isExpanded a false quando il mouse esce dall'area della sidebar.
   * Associata a (mouseleave) nel template.
   */
  onMouseLeave(): void {
    this.isExpanded = false;
  }

}
