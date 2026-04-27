// auth-guard.ts
import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth';
import { isPlatformBrowser } from '@angular/common';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // 1. Si pas connecté -> Login
  if (!authService.isLoggedIn()) {
    return router.parseUrl('/login');
  }

  const user = authService.getUser();
  const requiredRole = route.data['role'];

  // 2. Si l'utilisateur est ADMIN, il a TOUS les droits
  if (user?.role === 'ADMIN') {
    return true;
  }

  // 3. Si la route est réservée aux ADMINs et que l'utilisateur n'est pas ADMIN
  if (requiredRole === 'ADMIN' && user?.role !== 'ADMIN') {
    return router.parseUrl('/explorer'); // Redirection vers l'accueil utilisateur
  }

  // 4. Dans tous les autres cas (ex: route "USER" pour un utilisateur connecté)
  return true;
};
