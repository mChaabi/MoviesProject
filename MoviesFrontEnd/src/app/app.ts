// app.ts
import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
// app.ts
export class App implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  title = 'MoviesHub';

  showNav() {
    const isAuthPage = this.router.url.includes('/login') || this.router.url.includes('/register');
    return this.isLoggedIn() && !isAuthPage;
  }

  ngOnInit() {
    // SUPPRIMEZ TOUT LE CONTENU DE ngOnInit QUI FAIT DES this.router.navigate(['/login'])
    // L'authGuard s'en occupe déjà !
  }

  isLoggedIn() {
    return this.authService.isLoggedIn(); // Utilisez la même méthode que le guard
  }

  isAdmin() {
    return this.authService.getUser()?.role === 'ADMIN';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
