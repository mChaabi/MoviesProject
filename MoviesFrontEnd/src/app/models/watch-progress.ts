// 🆕 NOUVEAU — À mettre dans src/app/models/watch-progress.ts
export interface WatchProgress {
  id?: number;
  userId: number;
  episodeId?: number;
  movieId?: number;
  progressPercent: number;
  lastPositionSeconds: number;
  completed: boolean;
}
