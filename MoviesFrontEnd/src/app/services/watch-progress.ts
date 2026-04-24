// 🆕 NOUVEAU — src/app/services/watch-progress-service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WatchProgress } from '../models/watch-progress';

@Injectable({ providedIn: 'root' })
export class WatchProgressService {
  private api = 'http://localhost:8080/api/progress';

  constructor(private http: HttpClient) {}

  getMovieProgress(userId: number, movieId: number): Observable<WatchProgress> {
    const params = new HttpParams().set('userId', userId).set('movieId', movieId);
    return this.http.get<WatchProgress>(`${this.api}/movie`, { params });
  }

  getEpisodeProgress(userId: number, episodeId: number): Observable<WatchProgress> {
    const params = new HttpParams().set('userId', userId).set('episodeId', episodeId);
    return this.http.get<WatchProgress>(`${this.api}/episode`, { params });
  }

  saveMovieProgress(userId: number, movieId: number, percent: number, positionSeconds: number): Observable<WatchProgress> {
    const params = new HttpParams()
      .set('userId', userId).set('movieId', movieId)
      .set('percent', percent).set('positionSeconds', positionSeconds);
    return this.http.post<WatchProgress>(`${this.api}/movie/save`, null, { params });
  }

  saveEpisodeProgress(userId: number, episodeId: number, percent: number, positionSeconds: number): Observable<WatchProgress> {
    const params = new HttpParams()
      .set('userId', userId).set('episodeId', episodeId)
      .set('percent', percent).set('positionSeconds', positionSeconds);
    return this.http.post<WatchProgress>(`${this.api}/episode/save`, null, { params });
  }
}
