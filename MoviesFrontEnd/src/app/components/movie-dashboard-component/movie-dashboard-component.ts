// ✅ MODIFIÉ — src/app/components/movie-dashboard/movie-dashboard-component.ts
import { Component, signal, inject, OnInit, computed, ElementRef , ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { MovieService } from '../../services/movie-service';
import { Movie, MediaType } from '../../models/movie';
import { CategoryService } from '../../services/category-service';
import { Category } from '../../models/category';
import { LikeService } from '../../services/like-service';
import { LikeRequest } from '../../models/like-request';
import { CommentService } from '../../services/comment-service';
import { Comment } from '../../models/comment';
import { FollowService } from '../../services/follow-service';
import { WatchlistService } from '../../services/watchlist';
import { WatchProgressService } from '../../services/watch-progress';
import { MovieTypeCountPipe } from '../../pipes/movie-type-count-pipe';
import { RouterModule } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-movie-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule,MovieTypeCountPipe,RouterModule],
  templateUrl: './movie-dashboard-component.html',
  styleUrls: ['./movie-dashboard-component.css']
})
export class MovieDashboardComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef;
  private fb = inject(FormBuilder);
  private movieService = inject(MovieService);
  private categoryService = inject(CategoryService);
  private likeService = inject(LikeService);
  private commentService = inject(CommentService);
  private followService = inject(FollowService);
  private watchlistService = inject(WatchlistService);
  private watchProgressService = inject(WatchProgressService);
  private sanitizer = inject(DomSanitizer);
  private router = inject(Router);

  categories = signal<Category[]>([]);
  movies = signal<Movie[]>([]);
  isLoading = signal(false);
  selectedFile: File | null = null;

  currentUserId = 1;

  // 🆕 NOUVEAU — Filtre actif : 'ALL' | 'MOVIE' | 'SERIES'
  activeFilter = signal<'ALL' | MediaType>('ALL');

  // 🆕 NOUVEAU — Type de contenu en cours d'ajout
  addingType = signal<MediaType>('MOVIE');

  // 🆕 NOUVEAU — Films filtrés selon l'onglet actif
  filteredMovies = computed(() => {
    const filter = this.activeFilter();
    if (filter === 'ALL') return this.movies();
    return this.movies().filter(m => m.type === filter);
  });

  followsState = signal<Map<number, boolean>>(new Map());
  commentsState = signal<Map<number, Comment[]>>(new Map());
  newCommentText: { [key: number]: string } = {};
  likesState = signal<Map<number, { isLiked: boolean; count: number }>>(new Map());

  // 🆕 NOUVEAU — watchlist state
  watchlistState = signal<Set<number>>(new Set());

  // 🆕 NOUVEAU — progression de visionnage
  progressState = signal<Map<number, number>>(new Map());

 movieForm = this.fb.group({
  title: ['', [Validators.required, Validators.maxLength(100)]],
  description: ['', [Validators.maxLength(500)]],
  categoryId: [null, Validators.required],
  releaseYear: [null],
  rating: [null],
  durationMinutes: [null],
  videoUrl: ['']   // 🆕 AJOUTE CETTE LIGNE
});
  ngOnInit() {
    this.loadMovies();
    this.loadCategories();
  }

  // 🆕 NOUVEAU — Changer le filtre Films / Séries / Tous
  setFilter(filter: 'ALL' | MediaType) {
    this.activeFilter.set(filter);
  }

  // 🆕 NOUVEAU — Changer le type à ajouter
  setAddingType(type: MediaType) {
    this.addingType.set(type);
    // Rendre le fichier vidéo non requis pour les séries
    if (type === 'SERIES') {
      this.selectedFile = null;
    }
  }

  loadMovies() {
    this.movieService.getAllMovies().subscribe(data => {
      this.movies.set(data);
      data.forEach(movie => {
        if (movie.id) {
          this.loadLikeStatus(movie.id);
          this.loadComments(movie.id);
          this.checkWatchlist(movie.id);
          this.loadProgress(movie.id);
        }
        if (movie.authorId) {
          this.loadFollowStatus(movie.authorId);
        }
      });
    });
  }

  goToWatch(movie: Movie) {
  if (movie.type === 'SERIES') {
    this.router.navigate(['/series', movie.id]);
  } else {
    this.router.navigate(['/watch', movie.id]);
  }
}

getYoutubeThumbnail(url: string): string {
  let videoId = '';
  if (url.includes('youtu.be/')) {
    videoId = url.split('youtu.be/')[1].split('?')[0];
  } else if (url.includes('v=')) {
    videoId = url.split('v=')[1].split('&')[0];
  }
  return `https://img.youtube.com/vi/${videoId}/hqdefault.jpg`;
}

  // 🆕 NOUVEAU — Vérifier si dans la watchlist
  checkWatchlist(movieId: number) {
    this.watchlistService.checkInWatchlist(this.currentUserId, movieId).subscribe(inList => {
      this.watchlistState.update(set => {
        const newSet = new Set(set);
        if (inList) newSet.add(movieId);
        return newSet;
      });
    });
  }

  // 🆕 NOUVEAU — Toggle watchlist
  onWatchlistToggle(movieId: number) {
    const inList = this.isInWatchlist(movieId);
    if (inList) {
      this.watchlistService.removeFromWatchlist(this.currentUserId, movieId).subscribe(() => {
        this.watchlistState.update(set => { const s = new Set(set); s.delete(movieId); return s; });
      });
    } else {
      this.watchlistService.addToWatchlist(this.currentUserId, movieId).subscribe(() => {
        this.watchlistState.update(set => { const s = new Set(set); s.add(movieId); return s; });
      });
    }
  }

  isInWatchlist(movieId: number): boolean {
    return this.watchlistState().has(movieId);
  }

  // 🆕 NOUVEAU — Charger la progression de visionnage
  loadProgress(movieId: number) {
    this.watchProgressService.getMovieProgress(this.currentUserId, movieId).subscribe(p => {
      this.progressState.update(map => {
        const m = new Map(map);
        m.set(movieId, p.progressPercent);
        return m;
      });
    });
  }

  getProgress(movieId: number): number {
    return this.progressState().get(movieId) ?? 0;
  }

  // ✅ EXISTANT — inchangé
  loadFollowStatus(authorId: number) {
    if (authorId === this.currentUserId) return;
    this.followService.checkFollowStatus(this.currentUserId, authorId).subscribe(isFollowing => {
      this.followsState.update(map => { const m = new Map(map); m.set(authorId, isFollowing); return m; });
    });
  }

  onFollowToggle(authorId: number) {
    const following = this.isFollowing(authorId);
    const action$ = following
      ? this.followService.unfollow(this.currentUserId, authorId)
      : this.followService.follow(this.currentUserId, authorId);
    action$.subscribe({
      next: () => this.followsState.update(map => { const m = new Map(map); m.set(authorId, !following); return m; }),
      error: err => console.error(err)
    });
  }

  isFollowing(authorId: number): boolean {
    return this.followsState().get(authorId) ?? false;
  }

  // ✅ EXISTANT — inchangé
  loadComments(movieId: number) {
    this.commentService.getByMovie(movieId).subscribe(comments => {
      this.commentsState.update(map => { const m = new Map(map); m.set(movieId, comments); return m; });
    });
  }

  onAddComment(movieId: number) {
    const content = this.newCommentText[movieId];
    if (!content?.trim()) return;
    const commentData: Comment = { content: content.trim(), userId: this.currentUserId, movieId, createdAt: new Date() };
    this.commentService.create(movieId, commentData).subscribe(saved => {
      this.commentsState.update(map => {
        const m = new Map(map);
        m.set(movieId, [...(m.get(movieId) || []), saved]);
        return m;
      });
      this.newCommentText[movieId] = '';
    });
  }

  getComments(movieId: number): Comment[] {
    return this.commentsState().get(movieId) || [];
  }

  // ✅ EXISTANT — inchangé
  loadLikeStatus(movieId: number) {
    this.likeService.getLikesCount(movieId).subscribe(count => {
      this.likeService.checkLikeStatus(this.currentUserId, movieId).subscribe(isLiked => {
        this.likesState.update(map => { const m = new Map(map); m.set(movieId, { isLiked, count }); return m; });
      });
    });
  }

  onLikeToggle(movieId: number) {
    const request: LikeRequest = { userId: this.currentUserId, movieId };
    this.likeService.toggleLike(request).subscribe(() => {
      this.likesState.update(map => {
        const m = new Map(map);
        const cur = m.get(movieId);
        if (cur) m.set(movieId, { isLiked: !cur.isLiked, count: !cur.isLiked ? cur.count + 1 : cur.count - 1 });
        return m;
      });
    });
  }

  triggerFileInput(event: Event) {
    // Si c'est un film et qu'aucun fichier n'est sélectionné
    if (this.addingType() === 'MOVIE' && !this.selectedFile) {
      // Empêche la soumission du formulaire
      event.preventDefault();
      // Ouvre la fenêtre de sélection de fichier
      this.fileInput.nativeElement.click();
    }
    // Sinon, le formulaire se soumettra normalement via (ngSubmit)
  }

  getLikeInfo(movieId: number) {
    return this.likesState().get(movieId) || { isLiked: false, count: 0 };
  }

// Dans movie-dashboard-component.ts
loadCategories() {
  this.categoryService.getAllCategories().subscribe({
    next: data => {
      console.log('Catégories chargées:', data); // Vérifiez dans la console F12
      this.categories.set(data);
    },
    error: err => console.error('Erreur de chargement:', err)
  });
}

  onFileDropped(event: DragEvent) {
  event.preventDefault();
  if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
    const file = event.dataTransfer.files[0];
    this.handleFile(file);
  }
}
// Modifiez votre fonction onFileSelected existante pour utiliser une logique commune
onFileSelected(event: any) {
  const file = event.target.files[0];
  if (file) {
    this.handleFile(file);
  }
}

// Logique commune de vérification
private handleFile(file: File) {
  const maxSize = 1024 * 1024 * 1024; // 1 Go
  if (file.size > maxSize) {
    alert('Fichier trop volumineux. Max 1 Go.');
    this.selectedFile = null;
    return;
  }
  this.selectedFile = file;
}

  onVideoProgress(event: any, movieId: number) {
  const video = event.target as HTMLVideoElement;
  const percent = Math.round((video.currentTime / video.duration) * 100);
  // Ta logique de sauvegarde ici...
}

isYoutube(url: string): boolean {
  return url.includes('youtube.com') || url.includes('youtu.be');
}

isExternalUrl(url: string): boolean {
  return url.startsWith('http');
}

safeUrl(url: string): SafeResourceUrl {
  let videoId = '';
  if (url.includes('youtu.be/')) {
    videoId = url.split('youtu.be/')[1].split('?')[0];
  } else if (url.includes('v=')) {
    videoId = url.split('v=')[1].split('&')[0];
  }
  return this.sanitizer.bypassSecurityTrustResourceUrl(
    `https://www.youtube.com/embed/${videoId}`
  );
}

 onAddMovie() {
  if (!this.movieForm.valid) return;

  const type = this.addingType();
  const videoUrl = this.movieForm.value.videoUrl;

  // Film : soit fichier, soit URL
  if (type === 'MOVIE' && !this.selectedFile && !videoUrl) {
    alert("Choisissez un fichier vidéo ou collez une URL.");
    return;
  }

  this.isLoading.set(true);

  const movieData: any = {
    title: this.movieForm.value.title,
    description: this.movieForm.value.description,
    categoryId: this.movieForm.value.categoryId,
    authorId: this.currentUserId,
    type,
    releaseYear: this.movieForm.value.releaseYear,
    rating: this.movieForm.value.rating,
    durationMinutes: this.movieForm.value.durationMinutes,
    videoUrl: videoUrl || null
  };

  // Si fichier uploadé → multipart, sinon → JSON simple
  const request$ = this.selectedFile
    ? this.movieService.createMovie(movieData, this.selectedFile)
    : this.movieService.createSeries(movieData);

  request$.subscribe({
    next: newMovie => {
      this.movies.update(prev => [newMovie, ...prev]);
      this.movieForm.reset();
      this.selectedFile = null;
      this.isLoading.set(false);
    },
    error: err => {
      console.error(err);
      this.isLoading.set(false);
    }
  });
}
}
