import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Validation {
  constructor(private http: HttpClient) {}

  getRegisterRules(): Observable<any> {
    return this.http.get('https://localhost:8334/validation/register');
  }

  getInfoRules(): Observable<any> {
    return this.http.get('https://localhost:8334/validation/info');
  }
}
