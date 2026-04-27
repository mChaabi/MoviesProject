// src/app/services/watchlist.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { WatchlistItem } from '../models/watchlist';

@Injectable({ providedIn: 'root' })
export class WatchlistService {
  // ✅ URL correcte avec /api
  private api = 'http://localhost:8080/api/watchlist';

  constructor(private http: HttpClient) {}

  getUserWatchlist(userId: number): Observable<WatchlistItem[]> {
    return this.http.get<WatchlistItem[]>(`${this.api}/user/${userId}`).pipe(
      catchError(() => of([]))
    );
  }

  checkInWatchlist(userId: number, movieId: number): Observable<boolean> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.get<boolean>(`${this.api}/check`, { params }).pipe(
      catchError(() => of(false))
    );
  }

  addToWatchlist(userId: number, movieId: number): Observable<WatchlistItem> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.post<WatchlistItem>(`${this.api}/add`, null, { params }).pipe(
      catchError(() => of({} as WatchlistItem))
    );
  }

  removeFromWatchlist(userId: number, movieId: number): Observable<void> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.delete<void>(`${this.api}/remove`, { params }).pipe(
      catchError(() => of(undefined as void))
    );
  }
}
