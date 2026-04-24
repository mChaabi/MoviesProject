

export type MediaType = 'MOVIE' | 'SERIES';

// 🆕 NOUVEAU — À mettre dans src/app/models/watchlist.ts
export interface WatchlistItem {
  id?: number;
  userId: number;
  movieId: number;
  movieTitle?: string;
  movieType?: MediaType;
  addedAt?: Date;
  title: string;       // <--- AJOUTE ÇA
  posterUrl: string;
}
