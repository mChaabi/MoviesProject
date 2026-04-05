import { Component, signal, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private router = inject(Router);

  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  // Définition du formulaire avec validations
  registerForm = this.fb.group({
    email: ['', [
      Validators.required,
      Validators.email,
      Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$') // Validation stricte
    ]],
    password: ['', [
      Validators.required,
      Validators.minLength(8),
      // Optionnel : impose au moins une majuscule et un chiffre
      Validators.pattern('^(?=.*[A-Z])(?=.*\\d).+$')
    ]]
  });

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading.set(true);
      // On envoie la valeur du formulaire (email + password)
      this.userService.register(this.registerForm.value as any).subscribe({
      next: (user) => {
        this.isLoading.set(false);
        // On stocke l'ID de l'utilisateur pour pouvoir ajouter des films plus tard
       localStorage.setItem('userId', user.id?.toString() ?? '');
        this.router.navigate(['/movies']); // Redirection vers le dashboard
      },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.message || 'Erreur serveur');
        }
      });
    }
  }
}
