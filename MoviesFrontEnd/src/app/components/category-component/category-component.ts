import { Component, OnInit, signal, inject, computed, Pipe, PipeTransform } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CategoryService } from '../../services/category-service';
import { MovieService } from '../../services/movie-service';
import { ThumbnailService } from '../../services/thumbnail';  // 🆕
import { Category } from '../../models/category';
import { Movie } from '../../models/movie';

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
  thumbnailService = inject(ThumbnailService);  // ✅ public pour le template

  categories = signal<Category[]>([]);
  movies = signal<Movie[]>([]);
  selectedCategoryId = signal<number | null>(null);
  typeFilter = signal<'ALL' | 'MOVIE' | 'SERIES'>('ALL');
  newCategoryName = signal('');
  newCategoryDescription = signal('');

  filteredMovies = computed(() => {
    const categoryId = this.selectedCategoryId();
    if (!categoryId) return this.movies();
    return this.movies().filter(m => m.categoryId === categoryId);
  });

  displayedMovies = computed(() => {
    const type = this.typeFilter();
    const movies = this.filteredMovies();
    if (type === 'ALL') return movies;
    return movies.filter(m => m.type === type);
  });

  ngOnInit() { this.loadData(); }

  loadData() {
    this.categoryService.getAllCategories().subscribe(data => this.categories.set(data));
    this.movieService.getAllMovies().subscribe(data => this.movies.set(data));
  }

  getCountByCategory(categoryId: number): number {
    return this.movies().filter(m => m.categoryId === categoryId).length;
  }

  getSelectedCategoryName(): string {
    return this.categories().find(c => c.id === this.selectedCategoryId())?.name || '';
  }

  getCategoryIcon(name: string): string {
    const n = name?.toLowerCase() || '';
    if (n.includes('action'))    return '💥';
    if (n.includes('drame'))     return '🎭';
    if (n.includes('comédie'))   return '😂';
    if (n.includes('horreur'))   return '👻';
    if (n.includes('sci'))       return '🚀';
    if (n.includes('romance'))   return '❤️';
    if (n.includes('thriller'))  return '🔥';
    if (n.includes('doc'))       return '📽️';
    if (n.includes('policier') || n.includes('crime')) return '🔍';
    if (n.includes('aventure'))  return '🗺️';
    return '🎬';
  }

  // ✅ Utilise ThumbnailService
  getThumbnail(movie: Movie): string | null {
    return this.thumbnailService.getThumbnail(movie);
  }

  goToMovie(movie: Movie) {
    if (movie.type === 'SERIES') this.router.navigate(['/series', movie.id]);
    else this.router.navigate(['/watch', movie.id]);
  }

  onAddCategory() {
    if (!this.newCategoryName().trim()) return;
    const newCat: Category = {
      name: this.newCategoryName(),
      description: this.newCategoryDescription()
    };
    this.categoryService.addCategory(newCat).subscribe({
      next: saved => {
        this.categories.update(prev => [...prev, saved]);
        this.newCategoryName.set('');
        this.newCategoryDescription.set('');
      },
      error: err => console.error(err)
    });
  }

  onSelectCategory(id: number | null) {
    this.selectedCategoryId.set(id);
    this.typeFilter.set('ALL');
  }

  onDeleteCategory(id: number) {
    if (!confirm('Supprimer cette catégorie ?')) return;
    this.categoryService.deleteCategory(id).subscribe(() => {
      this.categories.update(prev => prev.filter(c => c.id !== id));
      if (this.selectedCategoryId() === id) this.selectedCategoryId.set(null);
    });
  }
}
