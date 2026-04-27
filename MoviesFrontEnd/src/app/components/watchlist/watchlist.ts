import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { WatchlistService } from '../../services/watchlist';
import { WatchlistItem } from '../../models/watchlist';
import { MovieService } from '../../services/movie-service';
import { Movie } from '../../models/movie';
import { MovieTypeCountPipe } from '../../pipes/movie-type-count-pipe';
import { ThumbnailService } from '../../services/thumbnail';

interface WatchlistItemEnriched extends WatchlistItem {
  movieDetails?: Movie;
  isLoadingDetails?: boolean;
}

@Component({
  selector: 'app-watchlist',
  standalone: true,
  imports: [CommonModule, RouterModule, MovieTypeCountPipe],
  templateUrl: './watchlist.html',
  styleUrls: ['./watchlist.css']
})
export class WatchlistComponent implements OnInit {
  private watchlistService = inject(WatchlistService);
  private movieService = inject(MovieService);
  private router = inject(Router);
  thumbnailService = inject(ThumbnailService);  // ✅ public pour le template

  currentUserId = 1;
  watchlist = signal<WatchlistItemEnriched[]>([]);
  isLoading = signal(true);
  selectedMovie = signal<Movie | null>(null);
  activeFilter = signal<'ALL' | 'MOVIE' | 'SERIES'>('ALL');


  filteredList = computed(() => {
    const filter = this.activeFilter();
    const list = this.watchlist();
    if (filter === 'ALL') return list;
    return list.filter(item => item.movieType === filter);
  });

  ngOnInit() { this.loadWatchlist(); }

  loadWatchlist() {
    this.watchlistService.getUserWatchlist(this.currentUserId).subscribe({
      next: data => {
        const enriched: WatchlistItemEnriched[] = data.map(item => ({
          ...item, isLoadingDetails: true
        }));
        this.watchlist.set(enriched);
        this.isLoading.set(false);

        enriched.forEach((item, index) => {
          this.movieService.getMovieById(item.movieId).subscribe({
            next: (movie) => {
              this.watchlist.update(list => {
                const updated = [...list];
                updated[index] = { ...updated[index], movieDetails: movie, isLoadingDetails: false };
                return updated;
              });
            },
            error: () => {
              this.watchlist.update(list => {
                const updated = [...list];
                updated[index] = { ...updated[index], isLoadingDetails: false };
                return updated;
              });
            }
          });
        });
      },
      error: () => this.isLoading.set(false)
    });
  }

  // Dans watchlist.ts
openDetails(item: WatchlistItemEnriched) {
  if (item.movieDetails) {
    console.log('Détails du film :', item.movieDetails); // 🔍 Vérifie les noms des propriétés ici
    this.selectedMovie.set(item.movieDetails);
  }
}

  setFilter(f: 'ALL' | 'MOVIE' | 'SERIES') { this.activeFilter.set(f); }


  closeModal() { this.selectedMovie.set(null); }

  goWatch(movie: Movie) {
    this.closeModal();
    if (movie.type === 'SERIES') this.router.navigate(['/series', movie.id]);
    else this.router.navigate(['/watch', movie.id]);
  }

  removeItem(movieId: number) {
    this.watchlistService.removeFromWatchlist(this.currentUserId, movieId).subscribe(() => {
      this.watchlist.update(prev => prev.filter(item => item.movieId !== movieId));
      if (this.selectedMovie()?.id === movieId) this.closeModal();
    });
  }

  // ✅ Utilise ThumbnailService
  getThumbnail(movie: Movie): string | null {
    return this.thumbnailService.getThumbnail(movie);
  }
}
