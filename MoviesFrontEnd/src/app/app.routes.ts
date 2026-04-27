// app.routes.ts
import { Routes } from '@angular/router';
import { MovieDashboardComponent } from './components/movie-dashboard-component/movie-dashboard-component';
import { CategoryComponent } from './components/category-component/category-component';
import { SeriesDetailComponent } from './components/series-detail/series-detail';
import { WatchlistComponent } from './components/watchlist/watchlist';
import { WatchComponent } from './components/watch/watch';
import { StatsDashboardComponent } from './components/stats-dashboard/stats-dashboard';
import { authGuard } from './auth-guard';

export const routes: Routes = [

  // 1. Redirection par défaut vers login si on arrive sur le site
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // ── Pages publiques (sans guard) ──
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () => import('./components/register/register').then(m => m.Register)
  },

  // ── Pages ADMIN uniquement ──
  {
    path: 'admin/stats',
    component: StatsDashboardComponent,
    title: 'MoviesHub — Statistiques',
    canActivate: [authGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'categories',
    component: CategoryComponent,
    title: 'MoviesHub — Catégories',
    canActivate: [authGuard],
    data: { role: 'ADMIN' }
  },

  // ── Pages USER + ADMIN ──
  {
    path: 'explorer',
    component: MovieDashboardComponent,
    title: 'MoviesHub — Explorer',
    canActivate: [authGuard]
    // pas de data.role → accessible à tous les connectés
  },
  {
    path: 'watchlist',
    component: WatchlistComponent,
    title: 'MoviesHub — Ma Liste',
    canActivate: [authGuard]
  },
  {
    path: 'watch/:id',
    component: WatchComponent,
    title: 'MoviesHub — Visionnage',
    canActivate: [authGuard]
  },
  {
    path: 'series/:id',
    component: SeriesDetailComponent,
    title: 'MoviesHub — Série',
    canActivate: [authGuard]
  },

  // ── Fallback ──
  {
    path: '**',
    redirectTo: 'login'
  }
];
