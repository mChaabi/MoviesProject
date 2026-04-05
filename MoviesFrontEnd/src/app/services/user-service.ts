import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // L'URL contient /api car tu as mis server.servlet.context-path=/api
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  // Appelle GET /api/users
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // Appelle POST /api/users/add
  register(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/add`, user);
  }
}
