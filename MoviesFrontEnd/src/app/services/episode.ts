// 🆕 NOUVEAU — src/app/services/episode-service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Episode } from '../models/episode';

@Injectable({ providedIn: 'root' })
export class EpisodeService {
  private api = 'http://localhost:8080/api/episodes';

  constructor(private http: HttpClient) {}

  getEpisodesBySeason(seasonId: number): Observable<Episode[]> {
    return this.http.get<Episode[]>(`${this.api}/season/${seasonId}`);
  }

  getEpisodeById(id: number): Observable<Episode> {
    return this.http.get<Episode>(`${this.api}/${id}`);
  }

  createEpisode(data: Partial<Episode>, file: File): Observable<Episode> {
    const formData = new FormData();
    formData.append('episode', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    formData.append('file', file);
    return this.http.post<Episode>(`${this.api}/add`, formData);
  }

  deleteEpisode(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/delete/${id}`);
  }
}
