
import { Season } from './season';
export type MediaType = 'MOVIE' | 'SERIES';

export interface Movie {
  id?: number;
  title: string;
  videoUrl?: string;        // Nullable pour les séries
  description?: string;
  authorId?: number;
  authorName?: string;
  authorEmail?: string;
  categoryId?: number;
  categoryName?: string;
  genre: string;

  // 🆕 Nouveaux champs
  type: MediaType;          // 'MOVIE' ou 'SERIES'
  rating?: number;          // Note sur 10
  releaseYear?: number;
  durationMinutes?: number; // Pour les films
  seasons?: Season[];       // Pour les séries
  totalSeasons?: number;
   coverUrl?: string;   // 🆕
}
