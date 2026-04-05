import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FollowService {

  private apiUrl = 'http://localhost:8080/api/follows';

  constructor(private http: HttpClient) { }

  /**
   * Alterne entre s'abonner et se désabonner
   * @param followerId L'ID de l'utilisateur connecté
   * @param followingId L'ID de l'utilisateur à suivre
   */
// Dans follow-service.ts
follow(followerId: number, followingId: number): Observable<any> {
  return this.http.post(`${this.apiUrl}/${followerId}/to/${followingId}`, {});
}

unfollow(followerId: number, followingId: number): Observable<void> {
  return this.http.delete<void>(`${this.apiUrl}/${followerId}/from/${followingId}`);
}

checkFollowStatus(followerId: number, followingId: number): Observable<boolean> {
  return this.http.get<boolean>(`${this.apiUrl}/status/${followerId}/${followingId}`);
}
}
