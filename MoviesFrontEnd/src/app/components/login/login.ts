// login.ts
import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private router = inject(Router);

  loading = false;
  errorMessage = '';

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });


  ngOnInit() {
    // 🔥 Nettoie tout au chargement pour être sûr que la Nav disparaisse
    localStorage.clear();
    this.authService.logout();
  }

  onLogin() {
    if (this.loginForm.invalid) return;

    this.loading = true;
    this.errorMessage = '';

    const { email, password } = this.loginForm.value;

    this.userService.login({ email: email!, password: password! }).subscribe({
      next: (user) => {
        this.loading = false;

        // ✅ ÉTAPE CRUCIALE : On sauvegarde l'utilisateur dans le localStorage
        this.authService.saveUser(user);

        console.log("Connexion réussie pour le rôle :", user.role);

        // ✅ REDIRECTION
        if (user.role === 'ADMIN') {
          this.router.navigate(['/admin/stats']); // Vérifie que ce path correspond à ton app.routes.ts
        } else {
          this.router.navigate(['/explorer']);
        }
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = 'Email ou mot de passe incorrect.';
        console.error("Erreur de login", err);
      }
    });
  }
}
