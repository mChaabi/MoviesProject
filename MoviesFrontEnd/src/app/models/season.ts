
import { Episode } from './episode';

export interface Season {
  id?: number;
  seasonNumber: number;
  title?: string;
  description?: string;
  seriesId: number;
  episodes?: Episode[];
  totalEpisodes?: number;
}
