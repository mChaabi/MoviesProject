// Optionnel : Un petit interface pour les compteurs sur le profil
export interface FollowStats {
  followingId: number;
  followersCount: number;
  followingCount: number;
  isFollowing: boolean; // Pour savoir si l'utilisateur connecté suit déjà ce profil
}
