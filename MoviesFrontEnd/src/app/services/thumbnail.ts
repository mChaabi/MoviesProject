// src/app/services/thumbnail.service.ts
import { Injectable } from '@angular/core';
import { Movie } from '../models/movie';

@Injectable({ providedIn: 'root' })
export class ThumbnailService {

  /**
   * Retourne l'URL de la cover/thumbnail d'un film
   * Priorité : coverUrl → YouTube thumbnail → null
   */
  getThumbnail(movie: Movie): string | null {
    // 1. Cover image explicite (TMDB, etc.)
    if (movie.coverUrl && movie.coverUrl.trim()) {
      return movie.coverUrl;
    }

    // 2. YouTube thumbnail
    if (movie.videoUrl) {
      const ytId = this.extractYoutubeId(movie.videoUrl);
      if (ytId) return `https://img.youtube.com/vi/${ytId}/hqdefault.jpg`;

      // 3. Dailymotion thumbnail
      const dmId = this.extractDailymotionId(movie.videoUrl);
      if (dmId) return `https://www.dailymotion.com/thumbnail/video/${dmId}`;
    }

    return null;
  }

  extractYoutubeId(url: string): string | null {
    if (!url) return null;
    if (url.includes('youtu.be/')) return url.split('youtu.be/')[1].split('?')[0];
    if (url.includes('v=')) return url.split('v=')[1].split('&')[0];
    return null;
  }

  extractDailymotionId(url: string): string | null {
    if (!url) return null;
    if (url.includes('/video/')) return url.split('/video/')[1].split('?')[0].split('_')[0];
    if (url.includes('dai.ly/')) return url.split('dai.ly/')[1].split('?')[0];
    return null;
  }

  /**
   * Retourne une couleur de fond unique selon le titre
   * Pour les films sans cover
   */
  getPlaceholderColor(title: string): string {
    const colors = [
      '#1a2540', '#1a2030', '#201a30', '#2a1a20',
      '#1a2a20', '#2a2a1a', '#1a1a2a', '#2a1a2a'
    ];
    let hash = 0;
    for (let i = 0; i < title.length; i++) {
      hash = title.charCodeAt(i) + ((hash << 5) - hash);
    }
    return colors[Math.abs(hash) % colors.length];
  }

  /**
   * Initiales du titre pour le placeholder
   */
  getInitials(title: string): string {
    return title.split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase();
  }
}
