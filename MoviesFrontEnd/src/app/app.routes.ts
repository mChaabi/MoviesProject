import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register';
import { MovieDashboardComponent } from './components/movie-dashboard-component/movie-dashboard-component';
import { ProfileComponent } from './components/profile-component/profile-component';
import { CategoryComponent } from './components/category-component/category-component';
import { authGuard } from './auth-guard';


export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'categories', component: CategoryComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'movies', component: MovieDashboardComponent },
   { path: '', redirectTo: 'register', pathMatch: 'full' } ,
   { path: 'movies', component: MovieDashboardComponent, canActivate: [authGuard] }
];
