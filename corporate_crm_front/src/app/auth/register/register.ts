import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, FormBuilder, FormGroup, Validators, ReactiveFormsModule} from '@angular/forms'; // Per ngModel
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { Validation } from '../../services/validation';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register implements OnInit {
  form!: FormGroup;
  validationRules: any = {};
  errorMessage: string = '';

  constructor(
    private authService: Auth, private fb: FormBuilder, private router: Router, private validation: Validation
  ) {}

  ngOnInit(): void {
    //Recupera regole dal backend
    this.validation.getRegisterRules().subscribe(rules => {
      this.validationRules = rules;
      this.buildForm(rules);
    });
  }

  // 2️⃣ Costruisce dinamicamente il form
  private buildForm(rules: any) {
    const group: any = {};

    for (const field in rules) {
      const validators = [];
      const r = rules[field];

      if (r.required) validators.push(Validators.required);
      if (r.pattern) validators.push(Validators.pattern(r.pattern));
      if (r.email) validators.push(Validators.email);

      group[field] = ['', validators];
    }

    this.form = this.fb.group(group);
  }

  onRegister(): void {
    if (this.form.invalid) {
      this.errorMessage = 'Compila correttamente tutti i campi';
      return;
    }

    this.authService.register(this.form.value).subscribe({
      next: (response) => {
        console.log('Registrazione completata', response);
        this.router.navigate(['/login']);
      },

      error: (error) => {
        console.error('Errore durante la registrazione', error);

        // esempio: gestione errore backend per email già esistente
        if (error.status === 400 && error.error.email) {
          this.form.get('email')?.setErrors({ backend: error.error.email });
        } else {
          this.errorMessage = 'Registrazione fallita. Riprova.';
        }
      }
    });
  }
}
