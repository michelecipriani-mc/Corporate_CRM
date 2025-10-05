import { Component, Output, EventEmitter, OnInit, Input,  OnChanges, SimpleChanges } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './home-modal.html',
  styleUrl: './home-modal.css'
})
export class HomeModal implements OnInit, OnChanges {
  @Input() formData: any;
  @Output() datiCompilati = new EventEmitter<any>();
  @Output() chiudi = new EventEmitter<void>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.initForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['formData'] && !changes['formData'].firstChange) {
      this.initForm();
    }
  }

  initForm() {
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
      const datiAggiornati = { ...this.formData, ...this.form.value };
      this.datiCompilati.emit(datiAggiornati);
      this.chiudi.emit();
    }
  }

  onClose() {
    this.chiudi.emit();
  }
}
