import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie'; // Assurez-vous d'avoir l'interface

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private apiUrl = 'http://localhost:8080/api/movies';

  constructor(private http: HttpClient) { }

  // --- 1. RÉCUPÉRER TOUS LES FILMS ---
  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(this.apiUrl);
  }

  // --- 2. RÉCUPÉRER UN FILM PAR ID ---
  getMovieById(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.apiUrl}/${id}`);
  }

  getMoviesByCategory(categoryId: number): Observable<Movie[]> {
  return this.http.get<Movie[]>(`${this.apiUrl}/category/${categoryId}`);
}

  // --- 3. AJOUTER UN FILM (Multipart FormData) ---
  createMovie(movieDto: any, fileSource: File): Observable<Movie> {
    const formData = new FormData();

    // DANS LE SERVICE
    // On transforme le DTO en Blob JSON pour que Spring puisse le désérialiser
    formData.append('movie', new Blob([JSON.stringify(movieDto)], { type: 'application/json' }));
    formData.append('file', fileSource);

    return this.http.post<Movie>(`${this.apiUrl}/add`, formData);
  }

  // --- 4. METTRE À JOUR UN FILM ---
  updateMovie(id: number, movieDto: Movie): Observable<Movie> {
    return this.http.put<Movie>(`${this.apiUrl}/update/${id}`, movieDto);
  }

  // --- 5. SUPPRIMER UN FILM ---
  deleteMovie(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
}
