import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms'; // Ajout de FormsModule pour le [(ngModel)]
import { MovieService } from '../../services/movie-service';
import { Movie } from '../../models/movie';
import { CategoryService } from '../../services/category-service';
import { Category } from '../../models/category';
import { LikeService } from '../../services/like-service';
import { LikeRequest } from '../../models/like-request';
import { CommentService } from '../../services/comment-service';
import { Comment } from '../../models/comment';
import { FollowService } from '../../services/follow-service'; // 1. Renommé pour éviter le conflit avec le type global 'Comment'
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-movie-dashboard',
  standalone: true,
  // 2. Ajout de FormsModule pour gérer la saisie des commentaires facilement
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './movie-dashboard-component.html',
  styleUrls: ['./movie-dashboard-component.css']
})
export class MovieDashboardComponent implements OnInit {
  private fb = inject(FormBuilder);
  private movieService = inject(MovieService);
  private categoryService = inject(CategoryService);
  private likeService = inject(LikeService);
  private commentService = inject(CommentService);
  private followService = inject(FollowService);

  categories = signal<Category[]>([]);
  movies = signal<Movie[]>([]);
  isLoading = signal(false);
  selectedFile: File | null = null;
    mySubscriptions: Subscription[] = [];

  // Simulation de l'utilisateur connecté
  currentUserId = 1;

  followsState = signal<Map<number, boolean>>(new Map());

  // 3. Typage correct avec AppComment
  commentsState = signal<Map<number, Comment[]>>(new Map());

  // Stocke le texte du nouveau commentaire par ID de film
  newCommentText: { [key: number]: string } = {};

  likesState = signal<Map<number, { isLiked: boolean, count: number }>>(new Map());

  movieForm = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(100)]],
    description: ['', [Validators.maxLength(500)]],
    categoryId: [null, Validators.required]
  });

  ngOnInit() {
    this.loadMovies();
    this.loadCategories();
  }

  loadMovies() {
    this.movieService.getAllMovies().subscribe(data => {
      this.movies.set(data);
      data.forEach(movie => {
        if (movie.id) {
          this.loadLikeStatus(movie.id);
          this.loadComments(movie.id);
        }
        if (movie.authorId) {
          this.loadFollowStatus(movie.authorId);
        }
      });
    });
  }


  // 4. Fonction pour charger le statut initial depuis le Backend
  loadFollowStatus(authorId: number) {
    if (authorId === this.currentUserId) return; // Pas besoin de se suivre soi-même

    this.followService.checkFollowStatus(this.currentUserId, authorId).subscribe(isFollowing => {
      this.followsState.update(map => {
        const newMap = new Map(map);
        newMap.set(authorId, isFollowing);
        return newMap;
      });
    });
  }

onFollowToggle(authorId: number) {
  const currentlyFollowing = this.isFollowing(authorId);

  if (currentlyFollowing) {
    this.followService.unfollow(this.currentUserId, authorId).subscribe({
      next: () => this.updateFollowMap(authorId, false),
      error: (err) => console.error(err)
    });
  } else {
    this.followService.follow(this.currentUserId, authorId).subscribe({
      next: () => this.updateFollowMap(authorId, true),
      error: (err) => console.error(err)
    });
  }
}

private updateFollowMap(authorId: number, status: boolean) {
  this.followsState.update(map => {
    const newMap = new Map(map);
    newMap.set(authorId, status);
    return newMap;
  });
}

isFollowing(authorId: number): boolean {
    // On récupère la valeur, si elle n'existe pas (undefined), on retourne false
    return this.followsState().get(authorId) ?? false;
}
  loadComments(movieId: number) {
    this.commentService.getByMovie(movieId).subscribe(comments => {
      this.commentsState.update(map => {
        const newMap = new Map(map);
        newMap.set(movieId, comments);
        return newMap;
      });
    });
  }

  onAddComment(movieId: number) {
    const content = this.newCommentText[movieId];
    if (!content || !content.trim()) return;

    // 4. Construction de l'objet conforme à ton entité Java
    // Note: On envoie l'objet movie avec son ID car le backend attend une relation ManyToOne
    const commentData: Comment = {
      content: content.trim(),
      userId: this.currentUserId,
      movieId: movieId,
      createdAt: new Date()
    };

    this.commentService.create(movieId, commentData).subscribe(savedComment => {
      this.commentsState.update(map => {
        const newMap = new Map(map);
        const currentComments = newMap.get(movieId) || [];
        newMap.set(movieId, [...currentComments, savedComment]);
        return newMap;
      });
      this.newCommentText[movieId] = ''; // Reset
    });
  }

  // 5. Utilisation du type AppComment ici aussi
  getComments(movieId: number): Comment[] {
    return this.commentsState().get(movieId) || [];
  }

  loadLikeStatus(movieId: number) {
    this.likeService.getLikesCount(movieId).subscribe(count => {
      this.likeService.checkLikeStatus(this.currentUserId, movieId).subscribe(isLiked => {
        this.likesState.update(map => {
          const newMap = new Map(map);
          newMap.set(movieId, { isLiked, count });
          return newMap;
        });
      });
    });
  }

  onLikeToggle(movieId: number) {
    const request: LikeRequest = {
      userId: this.currentUserId,
      movieId: movieId
    };

    this.likeService.toggleLike(request).subscribe(() => {
      this.likesState.update(map => {
        const newMap = new Map(map);
        const current = newMap.get(movieId);
        if (current) {
          const newLiked = !current.isLiked;
          newMap.set(movieId, {
            isLiked: newLiked,
            count: newLiked ? current.count + 1 : current.count - 1
          });
        }
        return newMap;
      });
    });
  }

  getLikeInfo(movieId: number) {
    return this.likesState().get(movieId) || { isLiked: false, count: 0 };
  }

  loadCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Erreur catégories', err)
    });
  }

 onFileSelected(event: any) {
  const file: File = event.target.files[0];

  if (file) {
    // Calcul pour 500 Mo : 500 * 1024 (Ko) * 1024 (Octets)
    const maxSize = 1024 * 1024 * 1024;

    // Debugging : affiche la taille réelle dans la console du navigateur
    console.log("Taille détectée :", (file.size / (1024 * 1024)).toFixed(2), "Mo");

    if (file.size > maxSize) {
      alert("Le fichier est trop volumineux. La limite autorisée est de 500 Mo.");

      // Reset de l'input et du signal/variable
      event.target.value = '';
      this.selectedFile = null;
      return;
    }

    // Si tout est bon, on enregistre le fichier
    this.selectedFile = file;
    console.log(`Succès : ${file.name} est prêt à être envoyé.`);
  }
}
  onAddMovie() {
    if (this.movieForm.valid && this.selectedFile) {
      this.isLoading.set(true);

      const movieData = {
        title: this.movieForm.value.title,
        categoryId: this.movieForm.value.categoryId,
        description: this.movieForm.value.description,
        authorId: this.currentUserId
      };

      this.movieService.createMovie(movieData, this.selectedFile).subscribe({
        next: (newMovie) => {
          this.movies.update(prev => [newMovie, ...prev]);
          this.movieForm.reset();
          this.selectedFile = null;
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error("Erreur serveur :", err);
          this.isLoading.set(false);
        }
      });
    }
  }
}
