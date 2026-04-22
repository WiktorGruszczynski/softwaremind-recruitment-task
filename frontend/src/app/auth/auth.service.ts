import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient)
  private apiUrl = 'http://localhost:8080/api'

  login(credentials: any) {
    return this.http.post(
      `${this.apiUrl}/auth/login`, credentials
    )
  }
}
