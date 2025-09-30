import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = `${environment.apiUrl}/auth`;
  private tokenKey = 'auth_token';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  private refreshTokenKey = 'refresh_token';
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();


  constructor(private http: HttpClient) {}

  // Metodo per verificare se l'utente è già autenticato
  hasToken(): boolean {
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

  //Metodo verifica richiesta Login
  isLoginRequest(request: HttpRequest<any>): boolean {
    return request.url.includes(`${this.apiUrl}/login`);
  }

  // Refresh
  refreshToken(): Observable<any> {
    return this.http.post(`${this.apiUrl}/refresh`, {}, { withCredentials: true }).pipe(
      tap((response: any) => {
        if (response && response.accessToken) {
          this.setToken(response.accessToken);
        }
      })
    );
  }

  // Info personali utente
  getUserInfo(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/info/you`);
  }

  // Metodo per il logout
  logout(): void {
    this.clearToken();
    this.isAuthenticatedSubject.next(false);
    // eventualmente redirect alla pagina di login
  }

  // Metodo per ottenere il token
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Sostituire il token
  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  // Eliminare il token
  clearToken(): void {
    localStorage.removeItem(this.tokenKey);
  }
}
