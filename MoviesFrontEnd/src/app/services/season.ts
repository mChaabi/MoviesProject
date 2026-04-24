// 🆕 NOUVEAU — src/app/services/season-service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Season } from '../models/season';

@Injectable({ providedIn: 'root' })
export class SeasonService {
  private api = 'http://localhost:8080/api/seasons';

  constructor(private http: HttpClient) {}

  getSeasonsBySeries(seriesId: number): Observable<Season[]> {
    return this.http.get<Season[]>(`${this.api}/series/${seriesId}`);
  }

  createSeason(data: Partial<Season>): Observable<Season> {
    return this.http.post<Season>(`${this.api}/add`, data);
  }

  deleteSeason(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/delete/${id}`);
  }
}
