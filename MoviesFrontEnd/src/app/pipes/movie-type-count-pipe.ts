// 🆕 NOUVEAU — src/app/pipes/movie-type-count.pipe.ts
// Pipe pour compter les films ou séries dans le template

import { Pipe, PipeTransform } from '@angular/core';
import { Movie, MediaType } from '../models/movie';

@Pipe({ name: 'movieTypeCount', standalone: true })
export class MovieTypeCountPipe implements PipeTransform {
  // On utilise 'any' ou une interface commune pour que ça marche avec la Watchlist
  transform(items: any[] | null, type: string): number {
    if (!items) return 0;
    // On vérifie soit 'type' (pour Movie) soit 'movieType' (pour WatchlistItem)
    return items.filter(item => (item.type === type || item.movieType === type)).length;
  }
}
