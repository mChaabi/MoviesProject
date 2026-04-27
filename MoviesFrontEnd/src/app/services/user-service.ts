import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class UserService {
  private api = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // Login — envoie email + password, reçoit UserResponse avec le role
  login(credentials: { email: string; password: string }): Observable<User> {
    return this.http.post<User>(`${this.api}/login`, credentials);
  }

  register(data: { name: string; email: string; password: string }): Observable<User> {
    return this.http.post<User>(`${this.api}/register`, data);
  }
}
