import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home';
// import { Navbar } from "./navbar/navbar";
// import { Footer } from "./footer/footer";

export const routes: Routes = [


  // Rotte pubbliche (accessibili a tutti)
  { path: 'login', component: Login },
  { path: 'register', component: Register },

  //route iniziale
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  //route iniziale se l’utente inserisce un URL sbagliato
  { path: '**', redirectTo: 'login' },

  //Rotte da rivedere con Authgard
  // { path: 'navbar', component: Navbar },
  // { path: 'footer', component: Footer },
  { path: 'home', component: Home }

  


  // Rotte protette (accessibili solo agli utenti autenticati)
  // La home è la rotta radice e protetta
  /*{ path: '', component: Home, canActivate: [authGuard] }*/, 
  // Aggiungi qui altre rotte protette, es.
  // { path: 'customers', component: CustomerListComponent, canActivate: [authGuard] },

  // Rotta generica per le pagine non trovate (da mettere sempre per ultima)
  // Puoi reindirizzare alla home se l'utente è loggato o al login se non lo è
  //{ path: '**', redirectTo: '' }
];
