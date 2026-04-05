import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LikeRequest } from '../models/like-request';
import { LikeResponse } from '../models/like-response';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/likes';

  // Utilisation de LikeRequest pour le paramètre
  toggleLike(likeData: LikeRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/toggle`, likeData, { responseType: 'text' });
  }

  // Utilisation de LikeResponse pour le retour
  getUserFavorites(userId: number): Observable<LikeResponse[]> {
    return this.http.get<LikeResponse[]>(`${this.apiUrl}/user/${userId}`);
  }

  getLikesCount(movieId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/${movieId}`);
  }

  checkLikeStatus(userId: number, movieId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/status`, {
      params: { userId, movieId }
    });
  }
}
