// src/app/services/watch-progress.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { WatchProgress } from '../models/watch-progress';

@Injectable({ providedIn: 'root' })
export class WatchProgressService {
  private api = 'http://localhost:8080/api/progress';

  constructor(private http: HttpClient) {}

  getMovieProgress(userId: number, movieId: number): Observable<WatchProgress> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.get<WatchProgress>(`${this.api}/movie`, { params }).pipe(
      // ✅ Si erreur (403, 404...) → retourner 0% sans crasher
      catchError(() => of({ progressPercent: 0, lastPositionSeconds: 0, completed: false } as WatchProgress))
    );
  }

  getEpisodeProgress(userId: number, episodeId: number): Observable<WatchProgress> {
    const params = new HttpParams().set('userId', userId).set('episodeId', episodeId);
    return this.http.get<WatchProgress>(`${this.api}/episode`, { params }).pipe(
      catchError(() => of({ progressPercent: 0, lastPositionSeconds: 0, completed: false } as WatchProgress))
    );
  }

  saveMovieProgress(userId: number, movieId: number, percent: number, positionSeconds: number): Observable<any> {
    const params = new HttpParams()
      .set('userId', userId).set('movieId', movieId)
      .set('percent', percent).set('positionSeconds', positionSeconds);
    return this.http.post<any>(`${this.api}/movie/save`, null, { params }).pipe(
      catchError(() => of(null))
    );
  }

  saveEpisodeProgress(userId: number, episodeId: number, percent: number, positionSeconds: number): Observable<any> {
    const params = new HttpParams()
      .set('userId', userId).set('episodeId', episodeId)
      .set('percent', percent).set('positionSeconds', positionSeconds);
    return this.http.post<any>(`${this.api}/episode/save`, null, { params }).pipe(
      catchError(() => of(null))
    );
  }
}
