import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { MovieService } from '../../services/movie-service';
import { CommentService } from '../../services/comment-service';
import { LikeService } from '../../services/like-service';
import { FormsModule } from '@angular/forms';
import { Movie } from '../../models/movie';
import { Comment } from '../../models/comment';
import { LikeRequest } from '../../models/like-request';

@Component({
  selector: 'app-watch',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './watch.html',
  styleUrls: ['./watch.css']
})
export class WatchComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private movieService = inject(MovieService);
  private commentService = inject(CommentService);
  private likeService = inject(LikeService);
  private sanitizer = inject(DomSanitizer);

  currentUserId = 1;
  movieId!: number;
  movie = signal<Movie | null>(null);
  comments = signal<Comment[]>([]);
  likeInfo = signal<{ isLiked: boolean; count: number }>({ isLiked: false, count: 0 });
  newComment = '';

  ngOnInit() {
    this.movieId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadMovie();
    this.loadComments();
    this.loadLikes();
  }

  loadMovie() {
    this.movieService.getMovieById(this.movieId).subscribe(data => this.movie.set(data));
  }

  loadComments() {
    this.commentService.getByMovie(this.movieId).subscribe(data => this.comments.set(data));
  }

  loadLikes() {
    this.likeService.getLikesCount(this.movieId).subscribe(count => {
      this.likeService.checkLikeStatus(this.currentUserId, this.movieId).subscribe(isLiked => {
        this.likeInfo.set({ isLiked, count });
      });
    });
  }

  onLike() {
    const req: LikeRequest = { userId: this.currentUserId, movieId: this.movieId };
    this.likeService.toggleLike(req).subscribe(() => {
      const cur = this.likeInfo();
      this.likeInfo.set({
        isLiked: !cur.isLiked,
        count: !cur.isLiked ? cur.count + 1 : cur.count - 1
      });
    });
  }

  onAddComment() {
    if (!this.newComment.trim()) return;
    const data: Comment = { content: this.newComment.trim(), userId: this.currentUserId, movieId: this.movieId, createdAt: new Date() };
    this.commentService.create(this.movieId, data).subscribe(saved => {
      this.comments.update(prev => [...prev, saved]);
      this.newComment = '';
    });
  }

  isYoutube(url: string): boolean {
    return url.includes('youtube.com') || url.includes('youtu.be');
  }

  safeUrl(url: string): SafeResourceUrl {
    let videoId = '';
    if (url.includes('youtu.be/')) videoId = url.split('youtu.be/')[1].split('?')[0];
    else if (url.includes('v=')) videoId = url.split('v=')[1].split('&')[0];
    return this.sanitizer.bypassSecurityTrustResourceUrl(`https://www.youtube.com/embed/${videoId}?autoplay=1`);
  }
}
