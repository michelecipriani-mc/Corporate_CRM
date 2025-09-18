import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home';

export const routes: Routes = [

  { path: 'login', component: Login },
  { path: 'register', component: Register },
  // Rotte protette
  // { path: 'customers', component: CustomerListComponent },
  // { path: '', redirectTo: '/customers', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' } // Reindirizza tutto il resto al login

  { path: 'home', component: Home },
  // Aggiungi un reindirizzamento per l'URL base
  { path: '', redirectTo: '/', pathMatch: 'full' },
  // Reindirizza le rotte non trovate al login, a meno che l'utente non sia autenticato
  { path: '**', redirectTo: '/' } 

];
