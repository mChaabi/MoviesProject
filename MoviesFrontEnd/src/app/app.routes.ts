// ✅ MODIFIÉ — src/app/app.routes.ts
// Ajoute les nouvelles routes pour séries et watchlist

import { Routes } from '@angular/router';
import { MovieDashboardComponent } from './components/movie-dashboard-component/movie-dashboard-component';
import { CategoryComponent } from './components/category-component/category-component';
import { SeriesDetailComponent } from './components/series-detail/series-detail';
import { WatchlistComponent } from './components/watchlist/watchlist';
import { WatchComponent } from './components/watch/watch';

export const routes: Routes = [

  // ✅ EXISTANT — Page principale films/séries
  {
    path: '',
    component: MovieDashboardComponent,
    title: 'MoviesHub — Explorer'
  },

   { path: 'watch/:id',
     component: WatchComponent,
     title: 'MoviesHub — Visionnage'
   },

  // ✅ EXISTANT — Gestion des catégories
  {
    path: 'categories',
    component: CategoryComponent,
    title: 'MoviesHub — Catégories'
  },

  // 🆕 NOUVEAU — Détail d'une série (saisons + épisodes)
  {
    path: 'series/:id',
    component: SeriesDetailComponent,
    title: 'MoviesHub — Série'
  },

  // 🆕 NOUVEAU — Ma liste personnelle
  {
    path: 'watchlist',
    component: WatchlistComponent,
    title: 'MoviesHub — Ma Liste'
  },

  // Redirection des routes inconnues
  {
    path: '**',
    redirectTo: ''
  }
];
