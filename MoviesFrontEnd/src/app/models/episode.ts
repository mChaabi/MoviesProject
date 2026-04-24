// 🆕 NOUVEAU — À mettre dans src/app/models/episode.ts
export interface Episode {
  id?: number;
  episodeNumber: number;
  title: string;
  description?: string;
  videoUrl?: string;
  durationMinutes?: number;
  seasonId: number;
}
