// ✅ MODIFIÉ — src/app/services/movie-service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie, MediaType } from '../models/movie';

@Injectable({ providedIn: 'root' })
export class MovieService {
  private api = 'http://localhost:8080/api/movies';

  constructor(private http: HttpClient) {}

  // ✅ EXISTANT — inchangé
  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(this.api);
  }

  // 🆕 NOUVEAU — seulement les films
  getOnlyMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.api}/films`);
  }

  // 🆕 NOUVEAU — seulement les séries
  getOnlySeries(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.api}/series`);
  }

  // ✅ EXISTANT — inchangé
  getMovieById(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.api}/${id}`);
  }

  // ✅ EXISTANT — inchangé
  createMovie(data: any, file: File): Observable<Movie> {
    const formData = new FormData();
    formData.append('movie', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    formData.append('file', file);
    return this.http.post<Movie>(`${this.api}/add`, formData);
  }

  // 🆕 NOUVEAU — créer une série (sans fichier vidéo)
  createSeries(data: Partial<Movie>): Observable<Movie> {
    return this.http.post<Movie>(`${this.api}/series/add`, data);
  }

  // ✅ EXISTANT — inchangé
  updateMovie(id: number, data: Partial<Movie>): Observable<Movie> {
    return this.http.put<Movie>(`${this.api}/update/${id}`, data);
  }

  // ✅ EXISTANT — inchangé
  deleteMovie(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/delete/${id}`);
  }
}
