export interface Comment {
  id?: number;
  content: string;
  userId: number;
  movieId: number;
  createdAt?: Date;
}
