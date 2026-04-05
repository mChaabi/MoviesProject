export interface LikeResponse {
  id: number;
  userId: number;
  username: string;
  movieId: number;
  movieTitle: string;
  createdAt: string; // LocalDateTime est reçu sous forme de chaîne ISO
}
