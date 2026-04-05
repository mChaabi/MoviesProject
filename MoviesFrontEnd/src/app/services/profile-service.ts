import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Profile } from '../models/profile'; // Ton interface existante

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private http = inject(HttpClient);
  // URL de base avec le context-path /api
  private apiUrl = 'http://localhost:8080/api/profiles/save';

  saveProfile(userId: number, username: string, bio: string, avatar: File | null): Observable<Profile> {
    const formData = new FormData();

    // On prépare les données pour le @RequestParam du Backend
    formData.append('userId', userId.toString());
    formData.append('username', username);
    formData.append('bio', bio);

    if (avatar) {
      formData.append('avatar', avatar); // Le nom 'avatar' doit être identique au @RequestParam du controller
    }

    // On retourne un Observable de type Profile
    return this.http.post<Profile>(this.apiUrl, formData);
  }
}
