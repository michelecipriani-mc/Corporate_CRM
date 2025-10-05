import { Component, Output, EventEmitter, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Validation } from '../services/validation';

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
  validationRules: any = {};

  constructor(private fb: FormBuilder, private validation: Validation) { }

  ngOnInit() {
    //Recupera le regole dal backend
    this.validation.getInfoRules().subscribe(rules => {
      this.validationRules = rules;
      this.buildForm(); // ricostruisce il form con i campi dinamici
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['formData'] && !changes['formData'].firstChange) {
      this.buildForm();
    }
  }

  //Costruisce dinamicamente il form con validator
  private buildForm() {
    const group: any = {};

    for (const field in this.validationRules) {
      const validators = [];
      const r = this.validationRules[field];

      if (r.required) validators.push(Validators.required);
      if (r.pattern) validators.push(Validators.pattern(r.pattern));

      group[field] = [this.formData?.[field] || '', validators];
    }

    this.form = this.fb.group(group);
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

  get validationFields(): string[] {
    return this.validationRules ? Object.keys(this.validationRules) : [];
  }
}
