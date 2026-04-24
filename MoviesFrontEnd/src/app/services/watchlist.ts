// 🆕 NOUVEAU — src/app/services/watchlist-service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WatchlistItem } from '../models/watchlist';

@Injectable({ providedIn: 'root' })
export class WatchlistService {
  private api = 'http://localhost:8080/api/watchlist';

  constructor(private http: HttpClient) {}

  getUserWatchlist(userId: number): Observable<WatchlistItem[]> {
    return this.http.get<WatchlistItem[]>(`${this.api}/user/${userId}`);
  }

  checkInWatchlist(userId: number, movieId: number): Observable<boolean> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.get<boolean>(`${this.api}/check`, { params });
  }

  addToWatchlist(userId: number, movieId: number): Observable<WatchlistItem> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.post<WatchlistItem>(`${this.api}/add`, null, { params });
  }

  removeFromWatchlist(userId: number, movieId: number): Observable<void> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.delete<void>(`${this.api}/remove`, { params });
  }
}
