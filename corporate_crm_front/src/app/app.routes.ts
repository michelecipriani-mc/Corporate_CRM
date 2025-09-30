import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home';
import { authGuard } from './auth-guard';
// import { Navbar } from "./navbar/navbar";
// import { Footer } from "./footer/footer";

export const routes: Routes = [
  // Rotte pubbliche (accessibili a tutti)
  { path: 'login', component: Login },
  { path: 'register', component: Register },

  //route iniziale
  //{ path: '', redirectTo: 'login', pathMatch: 'full' },
  //route iniziale se l’utente inserisce un URL sbagliato
  //{ path: '**', redirectTo: 'login' },

  //Rotte da rivedere
  // { path: 'navbar', component: Navbar },
  // { path: 'footer', component: Footer },

  // Rotte protette (accessibili solo agli utenti autenticati)
  { path: 'home', component: Home, canActivate: [authGuard] },

  // esempio:
  // { path: 'customers', component: CustomerListComponent, canActivate: [authGuard] },

  // Rotta wildcard per le pagine non trovate (da mettere sempre per ultima)
  // Reindirizza alla home se l'utente è loggato o al login altrimenti
  { path: '**', pathMatch: 'full', redirectTo: 'home' },
];
