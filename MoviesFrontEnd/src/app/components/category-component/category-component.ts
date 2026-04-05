import { Component, OnInit, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoryService } from '../../services/category-service';
import { MovieService } from '../../services/movie-service';
import { Category } from '../../models/category';
import { Movie } from '../../models/movie';

@Component({
  selector: 'app-category-manager',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-component.html',
  styleUrls: ['./category-component.css']
})
export class CategoryComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private movieService = inject(MovieService);

  categories = signal<Category[]>([]);
  movies = signal<Movie[]>([]);
  selectedCategoryId = signal<number | null>(null);
  // Ajoute ce signal pour gérer la sélection du menu déroulant
  selectedCategoryIdForNewMovie = signal<number | null>(null);

  // Signaux pour le formulaire
  newCategoryName = signal('');
  newCategoryDescription = signal(''); // Nouveau champ

  filteredMovies = computed(() => {
    const categoryId = this.selectedCategoryId();
    if (!categoryId) return this.movies();
    return this.movies().filter(m => m.categoryId === categoryId);
  });

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.categoryService.getAllCategories().subscribe(data => this.categories.set(data));
    this.movieService.getAllMovies().subscribe(data => this.movies.set(data));
  }




  onAddCategory() {
    if (this.newCategoryName().trim()) {
      const newCat: Category = {
        name: this.newCategoryName(),
        description: this.newCategoryDescription() // Envoi de la description
      };

      this.categoryService.addCategory(newCat).subscribe({
        next: (savedCat) => {
          this.categories.update(prev => [...prev, savedCat]);
          // Reset du formulaire
          this.newCategoryName.set('');
          this.newCategoryDescription.set('');
        },
        error: (err) => console.error("Erreur 404 ou autre : vérifiez l'URL de l'API", err)
      });
    }
  }



  onSelectCategory(id: number | null) {
    this.selectedCategoryId.set(id);
  }

  onDeleteCategory(id: number) {
    if (confirm('Supprimer cette catégorie ?')) {
      this.categoryService.deleteCategory(id).subscribe(() => {
        this.categories.update(prev => prev.filter(c => c.id !== id));
        if (this.selectedCategoryId() === id) this.selectedCategoryId.set(null);
      });
    }
  }
}
