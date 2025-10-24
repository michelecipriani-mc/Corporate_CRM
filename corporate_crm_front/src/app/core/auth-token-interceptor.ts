import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Auth } from '../services/auth';
import { catchError, switchMap, throwError } from 'rxjs';

export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(Auth);
  const token = auth.getToken();

  // Aggiungi il header di autorizzazione solo se l'utente è autenticato
  // (= solo se il token è presente)
  const cloned =
    token == null || token == ''
      ? req
      : req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`),
        });

  return next(cloned).pipe(
    catchError((error: HttpErrorResponse) => {
      if (
        (error.status === 401 || error.status === 403) &&
        !auth.isLoginRequest(req) &&
        !req.url.includes('/auth/refresh')
      ) {
        return auth.refreshToken().pipe(
          switchMap((response) => {
            const newCloned = req.clone({
              headers: req.headers.set('Authorization', `Bearer ${response.accessToken}`),
            });
            return next(newCloned);
          }),
          catchError((refreshError) => {
            auth.logout();
            return throwError(() => refreshError);
          })
        );
      }
      return throwError(() => error);
    })
  );
};
