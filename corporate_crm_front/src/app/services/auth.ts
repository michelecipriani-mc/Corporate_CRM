import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private apiUrl = `${environment.apiUrl}/auth`;
  private tokenKey = 'auth_token';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) { }

  // Metodo per verificare se l'utente è già autenticato
  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  // Metodo per la registrazione
  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  // Metodo per il login
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response && response.accessToken) {
          localStorage.setItem(this.tokenKey, response.accessToken);
          this.isAuthenticatedSubject.next(true);
        }
      })
    );
  }

  // Metodo per il logout
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.isAuthenticatedSubject.next(false);
  }

  // Metodo per ottenere il token
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }
}
