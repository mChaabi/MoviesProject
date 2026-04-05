export interface Profile {
  id?: number;          // Long en Java
  bio: string;          // String
  fullName: string;     // String
  avatarUrl: File | null;  // String (Lien vers l'image stockée)
  userId: number;       // Long (L'ID de l'utilisateur lié)
}
