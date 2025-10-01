// auth-guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router, RedirectCommand } from '@angular/router';
import { Auth } from './services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  // Permetti accesso per utente autenticato
  if (authService.hasToken()) {
    return true;
  }

  // Reindirizza a /login
  return router.createUrlTree(['login']);
};
