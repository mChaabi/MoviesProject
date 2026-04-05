import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TagDetails } from '../models/tag-details';
import { Tag } from '../models/tag';

@Injectable({
  providedIn: 'root'
})
export class TagService {
  // Vérifie bien si ton controller est sur /api/tags ou /tags
  private apiUrl = 'http://localhost:8080/api/tags';

  constructor(private http: HttpClient) { }

  // GET : Récupérer tous les tags
  getAllTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>(this.apiUrl);
  }

  // GET : Récupérer un tag avec ses films
  getTagById(id: number): Observable<TagDetails> {
    return this.http.get<TagDetails>(`${this.apiUrl}/${id}`);
  }

  // POST : Créer un tag
  createTag(tag: Tag): Observable<Tag> {
    return this.http.post<Tag>(`${this.apiUrl}/add`, tag);
  }

  // DELETE : Supprimer un tag
  deleteTag(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
