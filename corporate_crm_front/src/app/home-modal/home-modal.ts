import { Component, Output, EventEmitter, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-modal',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './home-modal.html',
  styleUrl: './home-modal.css'
})

export class HomeModal implements OnInit {
  @Input() formData: any;
  @Output() datiNonCompilati = new EventEmitter<string[]>();
  @Output() datiCompilati = new EventEmitter<any>();
  @Output() chiudi = new EventEmitter<void>();

  campi : {
    cellulare: string | null;
    dataNascita: Date | null;
    indirizzo: string | null;
    citta: string | null;
    provincia: string | null;
    cap: string | null;
    codiceFiscale: string | null;
    iban: string | null;
  } = {
    cellulare: null,
    dataNascita: null,
    indirizzo: null,
    citta: null,
    provincia: null,
    cap: null,
    codiceFiscale: null,
    iban: null
  };

  ngOnInit() {
  const mancanti = Object.entries(this.campi)
    .filter(([key, value]) => {
      if (typeof value === 'string') {
        return !value; // vuoto
      }
      if (value instanceof Date) {
        return isNaN(value.getTime()); // data non valida
      }
      return true; // altri casi
    })
    .map(([key]) => key);

    if (mancanti.length > 0) {
      this.datiNonCompilati.emit(mancanti);
    }
  }


  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      cellulare: [this.formData?.cellulare || '', Validators.required],
      dataNascita: [this.formData?.dataNascita || '', Validators.required],
      indirizzo: [this.formData?.indirizzo || '', Validators.required],
      citta: [this.formData?.citta || '', Validators.required],
      provincia: [this.formData?.provincia || '', Validators.required],
      cap: [this.formData?.cap || '', Validators.required],
      codiceFiscale: [this.formData?.codiceFiscale || '', Validators.required],
      iban: [this.formData?.iban || '', Validators.required],
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.datiCompilati.emit(this.form.value); // passa i dati al padre
      this.chiudi.emit(); // chiudi la modale
    }
  }

  onClose() {
    this.chiudi.emit();
  }
}
