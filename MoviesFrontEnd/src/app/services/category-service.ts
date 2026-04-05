import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private http = inject(HttpClient);

  // L'URL doit correspondre au @RequestMapping("/api/categories") de ton Controller Java
  private apiUrl = 'http://localhost:8080/api/categories';

  // --- 1. RÉCUPÉRER TOUTES LES CATÉGORIES (GET) ---
  // Pour afficher la liste dans le formulaire de ton Dashboard
  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  // --- 2. AJOUTER UNE NOUVELLE CATÉGORIE (POST) ---
  // Pour créer une catégorie comme "Action", "Comédie", "Prison Break", etc.
  addCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(`${this.apiUrl}/add`, category);
  }

  // --- 3. SUPPRIMER UNE CATÉGORIE (DELETE) ---
  // Pour supprimer une catégorie via son ID
  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
}
