import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProfileService } from '../../services/profile-service';
import { Profile } from '../../models/profile';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile-component.html',
  styleUrls: ['./profile-component.css']
})
export class ProfileComponent {
  private fb = inject(FormBuilder);
  private profileService = inject(ProfileService);
  private router = inject(Router);

  // Signals pour l'état de l'interface
  imagePreview = signal<string | null>(null);
  isLoading = signal(false);
  selectedFile: File | null = null;

  // Formulaire réactif
  profileForm = this.fb.group({
    fullName: ['', [Validators.required, Validators.minLength(3)]],
    bio: ['', [Validators.maxLength(250)]]
  });

  // Gestion de l'aperçu de l'image
  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview.set(reader.result as string);
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit() {
    if (this.profileForm.invalid) return;

    this.isLoading.set(true);

    const userId = Number(localStorage.getItem('userId'));

   const profileData: Profile = {
  fullName: this.profileForm.value.fullName!,
  bio: this.profileForm.value.bio!,
  userId: userId,
  avatarUrl: this.selectedFile!
};

this.profileService
  .saveProfile(userId, profileData.fullName, profileData.bio, this.selectedFile !== undefined ? this.selectedFile : null)
  .subscribe({
    next: () => {
      this.isLoading.set(false);
      this.router.navigate(['/movies']);
    },
    error: (err) => {
      this.isLoading.set(false);
      console.error('Erreur sauvegarde profil', err);
    }
  });
  }

}
