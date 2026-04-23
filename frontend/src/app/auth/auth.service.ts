import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { UserResponse } from './models/user.model';

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

  register(credentials: any) {
    return this.http.post(
      `${this.apiUrl}/auth/register`, credentials
    )
  }

  whoami() {
    return this.http.get<UserResponse>(`${this.apiUrl}/users/me`) 
  }
}
