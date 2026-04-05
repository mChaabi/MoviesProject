import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Comment } from '../models/comment';

@Injectable({ providedIn: 'root' })
export class CommentService {
  private http = inject(HttpClient);
  // Note le /api au début
  private apiUrl = 'http://localhost:8080/api/comments';

  create(movieId: number, comment: any) {
    // On respecte l'URL du controller : /movie/{id}/add
    return this.http.post<Comment>(`${this.apiUrl}/movie/${movieId}/add`, comment);
  }

  getByMovie(movieId: number) {
    return this.http.get<Comment[]>(`${this.apiUrl}/movie/${movieId}`);
  }


  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}


