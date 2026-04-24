import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { WatchlistService } from '../../services/watchlist';
import { WatchlistItem } from '../../models/watchlist';
import { MovieService } from '../../services/movie-service';
import { Movie } from '../../models/movie';
import { MovieTypeCountPipe } from '../../pipes/movie-type-count-pipe';

// Interface enrichie avec les détails du film
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

  currentUserId = 1;
  watchlist = signal<WatchlistItemEnriched[]>([]);
  isLoading = signal(true);
  selectedMovie = signal<Movie | null>(null); // Pour le modal de détails

  activeFilter = signal<'ALL' | 'MOVIE' | 'SERIES'>('ALL');

  filteredList() {
    const filter = this.activeFilter();
    if (filter === 'ALL') return this.watchlist();
    return this.watchlist().filter(item => item.movieType === filter);
  }

  ngOnInit() {
    this.loadWatchlist();
  }

  loadWatchlist() {
    this.watchlistService.getUserWatchlist(this.currentUserId).subscribe({
      next: data => {
        // Charger les détails de chaque film
        const enriched: WatchlistItemEnriched[] = data.map(item => ({
          ...item,
          isLoadingDetails: true
        }));
        this.watchlist.set(enriched);
        this.isLoading.set(false);

        // Charger les détails de chaque film en parallèle
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

  setFilter(f: 'ALL' | 'MOVIE' | 'SERIES') {
    this.activeFilter.set(f);
  }

  // Ouvrir le modal de détails
  openDetails(item: WatchlistItemEnriched) {
    if (item.movieDetails) {
      this.selectedMovie.set(item.movieDetails);
    }
  }

  // Fermer le modal
  closeModal() {
    this.selectedMovie.set(null);
  }

  // Naviguer vers la page de visionnage
  goWatch(movie: Movie) {
    this.closeModal();
    if (movie.type === 'SERIES') {
      this.router.navigate(['/series', movie.id]);
    } else {
      this.router.navigate(['/watch', movie.id]);
    }
  }

  removeItem(movieId: number) {
    this.watchlistService.removeFromWatchlist(this.currentUserId, movieId).subscribe(() => {
      this.watchlist.update(prev => prev.filter(item => item.movieId !== movieId));
      // Fermer le modal si c'est le film supprimé
      if (this.selectedMovie()?.id === movieId) this.closeModal();
    });
  }

  getThumbnail(movie: Movie): string | null {
    if (!movie.videoUrl) return null;
    if (movie.videoUrl.includes('youtu.be/')) {
      const id = movie.videoUrl.split('youtu.be/')[1].split('?')[0];
      return `https://img.youtube.com/vi/${id}/hqdefault.jpg`;
    }
    if (movie.videoUrl.includes('v=')) {
      const id = movie.videoUrl.split('v=')[1].split('&')[0];
      return `https://img.youtube.com/vi/${id}/hqdefault.jpg`;
    }
    return null;
  }
}
