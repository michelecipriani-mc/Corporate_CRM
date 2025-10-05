import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Validation {
  constructor(private http: HttpClient) {}

  getRegisterRules(): Observable<any> {
    return this.http.get('http://localhost:8080/validation/register');
  }

  getInfoRules(): Observable<any> {
    return this.http.get('http://localhost:8080/validation/info');
  }
}
