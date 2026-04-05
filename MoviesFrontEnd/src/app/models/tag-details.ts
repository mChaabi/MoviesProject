import { Tag } from './tag';
import { MovieSummary } from './movie-summary';

export interface TagDetails extends Tag {
  movies: MovieSummary[];
}
