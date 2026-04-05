export interface Movie {
  id?: number;
  title: string;
  description: string;
  videoUrl: string;
  authorId: number;
  authorName?: string;
  authorEmail?: string;
  categoryId: number;
  tags?: { id: number, label: string }[];
}
