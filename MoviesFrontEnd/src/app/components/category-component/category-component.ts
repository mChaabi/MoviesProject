// category-component.ts
import { Component, OnInit, signal, inject, computed, Pipe, PipeTransform } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CategoryService } from '../../services/category-service';
import { MovieService } from '../../services/movie-service';
import { Category } from '../../models/category';
import { Movie } from '../../models/movie';

// Pipe local pour compter par type dans le template catégorie
@Pipe({ name: 'movieTypeCountCat', standalone: true })
export class MovieTypeCountCatPipe implements PipeTransform {
  transform(movies: Movie[], type: 'MOVIE' | 'SERIES'): number {
    return movies.filter(m => m.type === type).length;
  }
}

@Component({
  selector: 'app-category-manager',
  standalone: true,
  imports: [CommonModule, FormsModule, MovieTypeCountCatPipe],
  templateUrl: './category-component.html',
  styleUrls: ['./category-component.css']
})
export class CategoryComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private movieService = inject(MovieService);
  private router = inject(Router);

  categories = signal<Category[]>([]);
  movies = signal<Movie[]>([]);
  selectedCategoryId = signal<number | null>(null);

  // Filtre type (Film / Série / Tout)
  typeFilter = signal<'ALL' | 'MOVIE' | 'SERIES'>('ALL');

  newCategoryName = signal('');
  newCategoryDescription = signal('');

  // Films filtrés par catégorie
  filteredMovies = computed(() => {
    const categoryId = this.selectedCategoryId();
    if (!categoryId) return this.movies();
    return this.movies().filter(m => m.categoryId === categoryId);
  });

  // Films filtrés par catégorie ET type
  displayedMovies = computed(() => {
    const type = this.typeFilter();
    const movies = this.filteredMovies();
    if (type === 'ALL') return movies;
    return movies.filter(m => m.type === type);
  });

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.categoryService.getAllCategories().subscribe(data => this.categories.set(data));
    this.movieService.getAllMovies().subscribe(data => this.movies.set(data));
  }

  // Nombre de films par catégorie
  getCountByCategory(categoryId: number): number {
    return this.movies().filter(m => m.categoryId === categoryId).length;
  }

  // Nom de la catégorie sélectionnée
  getSelectedCategoryName(): string {
    const cat = this.categories().find(c => c.id === this.selectedCategoryId());
    return cat?.name || '';
  }

  // Icône selon le nom de la catégorie
  getCategoryIcon(name: string): string {
    const n = name?.toLowerCase() || '';
    if (n.includes('action'))      return '💥';
    if (n.includes('drame') || n.includes('drama')) return '🎭';
    if (n.includes('comédie') || n.includes('comedie')) return '😂';
    if (n.includes('horror') || n.includes('horreur')) return '👻';
    if (n.includes('sci') || n.includes('science')) return '🚀';
    if (n.includes('romance') || n.includes('roman')) return '❤️';
    if (n.includes('thriller'))    return '🔥';
    if (n.includes('doc'))         return '📽️';
    if (n.includes('animation'))   return '🎨';
    if (n.includes('sport'))       return '⚽';
    if (n.includes('crime') || n.includes('policier')) return '🔍';
    if (n.includes('fantasy') || n.includes('fantaisie')) return '🧙';
    if (n.includes('aventure'))    return '🗺️';
    if (n.includes('histoire') || n.includes('histor')) return '📜';
    return '🎬';
  }

  // Thumbnail YouTube si disponible
  getThumbnail(movie: Movie): string | null {
    if (!movie.videoUrl) return null;
    let videoId = '';
    if (movie.videoUrl.includes('youtu.be/')) {
      videoId = movie.videoUrl.split('youtu.be/')[1].split('?')[0];
    } else if (movie.videoUrl.includes('v=')) {
      videoId = movie.videoUrl.split('v=')[1].split('&')[0];
    }
    if (!videoId) return null;
    return `https://img.youtube.com/vi/${videoId}/hqdefault.jpg`;
  }

  // Navigation vers le film
  goToMovie(movie: Movie) {
    if (movie.type === 'SERIES') {
      this.router.navigate(['/series', movie.id]);
    } else {
      this.router.navigate(['/watch', movie.id]);
    }
  }

  onAddCategory() {
    if (!this.newCategoryName().trim()) return;
    const newCat: Category = {
      name: this.newCategoryName(),
      description: this.newCategoryDescription()
    };
    this.categoryService.addCategory(newCat).subscribe({
      next: (saved) => {
        this.categories.update(prev => [...prev, saved]);
        this.newCategoryName.set('');
        this.newCategoryDescription.set('');
      },
      error: err => console.error(err)
    });
  }

  onSelectCategory(id: number | null) {
    this.selectedCategoryId.set(id);
    this.typeFilter.set('ALL'); // Reset le filtre type à chaque changement de catégorie
  }

  onDeleteCategory(id: number) {
    if (!confirm('Supprimer cette catégorie ?')) return;
    this.categoryService.deleteCategory(id).subscribe(() => {
      this.categories.update(prev => prev.filter(c => c.id !== id));
      if (this.selectedCategoryId() === id) this.selectedCategoryId.set(null);
    });
  }
}
