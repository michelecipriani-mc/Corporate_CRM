import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

export const routes: Routes = [

  { path: 'login', component: Login },
  { path: 'register', component: Register },
  // Rotte protette
  // { path: 'customers', component: CustomerListComponent },
  // { path: '', redirectTo: '/customers', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' } // Reindirizza tutto il resto al login

];
