// src/app/services/auth.ts
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private USER_KEY = 'movieshub_user';

isAuthenticated(): boolean {
  const user = localStorage.getItem('currentUser');
  return !!user; // Retourne true UNIQUEMENT si l'objet existe
}
  // Sauvegarder l'utilisateur après login
  saveUser(user: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  // Récupérer l'utilisateur connecté
  getUser(): any {
    const data = localStorage.getItem(this.USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  // Vérifier si connecté
  isLoggedIn(): boolean {
    return !!this.getUser();
  }

  // Vérifier si ADMIN
  isAdmin(): boolean {
    const user = this.getUser();
    return user?.role === 'ADMIN';
  }

  // Récupérer l'ID de l'utilisateur connecté
  getUserId(): number {
    return this.getUser()?.id ?? 1;
  }

  // Déconnexion
  logout(): void {
    localStorage.removeItem(this.USER_KEY);
  }
}
