import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MovieService } from '../../services/movie-service';
import { Movie } from '../../models/movie';

@Component({
  selector: 'app-stats-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './stats-dashboard.html',
  styleUrls: ['./stats-dashboard.css']
})
export class StatsDashboardComponent implements OnInit {
  private movieService = inject(MovieService);
  private router = inject(Router);

  movies = signal<Movie[]>([]);
  isLoading = signal(true);

  today = new Date().toLocaleDateString('fr-FR', {
    weekday: 'long', day: 'numeric', month: 'long', year: 'numeric'
  });

  // KPIs
  totalMovies  = computed(() => this.movies().filter(m => m.type === 'MOVIE').length);
  totalSeries  = computed(() => this.movies().filter(m => m.type === 'SERIES').length);
  totalContent = computed(() => this.movies().length);
  ratedCount   = computed(() => this.movies().filter(m => m.rating).length);

  totalSeasons = computed(() =>
    this.movies().reduce((sum, m) => sum + (m.totalSeasons || 0), 0)
  );

  recentMovies = computed(() => {
    const oneWeekAgo = new Date();
    oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);
    return this.movies().filter(m => m.type === 'MOVIE').length; // approximation
  });

  categoriesCount = computed(() => {
    const cats = new Set(this.movies().map(m => m.categoryName).filter(Boolean));
    return cats.size;
  });

  avgRating = computed(() => {
    const rated = this.movies().filter(m => m.rating);
    if (!rated.length) return '—';
    return (rated.reduce((s, m) => s + (m.rating || 0), 0) / rated.length).toFixed(1);
  });

  topRated = computed(() =>
    [...this.movies()]
      .filter(m => m.rating)
      .sort((a, b) => (b.rating || 0) - (a.rating || 0))
      .slice(0, 5)
  );

  recentContent = computed(() => [...this.movies()].slice(0, 8));

  // Donut chart
  totalArc = computed(() => Math.PI * 2 * 70); // circonférence
  movieArc = computed(() => {
    const total = this.totalContent();
    if (!total) return 0;
    return (this.totalMovies() / total) * 2 * Math.PI * 70;
  });
  seriesArc = computed(() => {
    const total = this.totalContent();
    if (!total) return 0;
    return (this.totalSeries() / total) * 2 * Math.PI * 70;
  });

  // Bar chart catégories
  colors = ['#3d8ef0','#a855f7','#e8b84b','#22d3a0','#ef4444','#f97316','#ec4899'];

  byCategory = computed(() => {
    const map = new Map<string, number>();
    this.movies().forEach(m => {
      const cat = m.categoryName || 'Sans catégorie';
      map.set(cat, (map.get(cat) || 0) + 1);
    });
    return Array.from(map.entries())
      .map(([name, count], i) => ({ name, count, color: this.colors[i % this.colors.length] }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 7);
  });

  maxCategoryCount = computed(() =>
    Math.max(...this.byCategory().map(c => c.count), 1)
  );

  // Histogramme notes
  ratingBuckets = computed(() => {
    const buckets = [
      { label: '1-2', min: 1, max: 2.9, count: 0, height: 0 },
      { label: '3-4', min: 3, max: 4.9, count: 0, height: 0 },
      { label: '5-6', min: 5, max: 6.9, count: 0, height: 0 },
      { label: '7-8', min: 7, max: 8.9, count: 0, height: 0 },
      { label: '9-10', min: 9, max: 10, count: 0, height: 0 },
    ];
    this.movies().forEach(m => {
      if (m.rating) {
        const bucket = buckets.find(b => m.rating! >= b.min && m.rating! <= b.max);
        if (bucket) bucket.count++;
      }
    });
    const max = Math.max(...buckets.map(b => b.count), 1);
    buckets.forEach(b => b.height = Math.round((b.count / max) * 100));
    return buckets;
  });

  ngOnInit() {
    this.movieService.getAllMovies().subscribe({
      next: data => { this.movies.set(data); this.isLoading.set(false); },
      error: () => this.isLoading.set(false)
    });
  }

  goToMovie(movie: Movie) {
    if (movie.type === 'SERIES') this.router.navigate(['/series', movie.id]);
    else this.router.navigate(['/watch', movie.id]);
  }

  getThumbnail(movie: Movie): string | null {
    if (movie.coverUrl) return movie.coverUrl;
    if (!movie.videoUrl) return null;
    let id = '';
    if (movie.videoUrl.includes('youtu.be/')) id = movie.videoUrl.split('youtu.be/')[1].split('?')[0];
    else if (movie.videoUrl.includes('v=')) id = movie.videoUrl.split('v=')[1].split('&')[0];
    return id ? `https://img.youtube.com/vi/${id}/hqdefault.jpg` : null;
  }
}
