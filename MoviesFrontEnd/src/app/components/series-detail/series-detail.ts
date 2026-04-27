// 🆕 NOUVEAU — src/app/components/series-detail/series-detail-component.ts
import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MovieService } from '../../services/movie-service';
import { SeasonService } from '../../services/season';
import { EpisodeService } from '../../services/episode';
import { WatchProgressService } from '../../services/watch-progress';
import { Season } from '../../models/season';
import { Episode } from '../../models/episode';
import { Movie } from '../../models/movie';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';


@Component({
  selector: 'app-series-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './series-detail.html',
  styleUrls: ['./series-detail.css']
})
export class SeriesDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private movieService = inject(MovieService);
  private seasonService = inject(SeasonService);
  private episodeService = inject(EpisodeService);
  private watchProgressService = inject(WatchProgressService);
  private fb = inject(FormBuilder);
  private sanitizer = inject(DomSanitizer);

  currentUserId = 1;
  seriesId!: number;

  series = signal<Movie | null>(null);
  seasons = signal<Season[]>([]);
  selectedSeason = signal<Season | null>(null);
  episodes = signal<Episode[]>([]);
  activeEpisode = signal<Episode | null>(null);

  isLoadingEpisodes = signal(false);
  showAddSeason = signal(false);
  showAddEpisode = signal(false);
  selectedEpisodeFile: File | null = null;

  // Progression par épisode
  progressMap = signal<Map<number, number>>(new Map());

  seasonForm = this.fb.group({
    seasonNumber: [null, Validators.required],
    title: [''],
    description: ['']
  });

  episodeForm = this.fb.group({
    episodeNumber: [null, Validators.required],
    title: ['', Validators.required],
    description: [''],
    durationMinutes: [null]
  });


  // Détecte le type de source
getVideoType(url: string | undefined): 'youtube' | 'dailymotion' | 'vimeo' | 'mp4' | 'unknown' {
  if (!url) return 'unknown';
  if (url.includes('youtube.com') || url.includes('youtu.be')) return 'youtube';
  if (url.includes('dailymotion.com') || url.includes('dai.ly')) return 'dailymotion';
  if (url.includes('vimeo.com')) return 'vimeo';
  if (url.endsWith('.mp4') || url.includes('/uploads/')) return 'mp4';
  return 'unknown';
}


// Génère l'URL embed selon la source
safeEpisodeUrl(url: string | undefined): SafeHtml {
  if (!url) return this.sanitizer.bypassSecurityTrustResourceUrl('');

  const type = this.getVideoType(url);

  if (type === 'youtube') {
    let videoId = '';
    if (url.includes('youtu.be/')) {
      videoId = url.split('youtu.be/')[1].split('?')[0];
    } else if (url.includes('v=')) {
      videoId = url.split('v=')[1].split('&')[0];
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `https://www.youtube.com/embed/${videoId}?autoplay=1`
    );
  }

  if (type === 'dailymotion') {
    // https://www.dailymotion.com/video/x81myot → x81myot
    let videoId = '';
    if (url.includes('/video/')) {
      videoId = url.split('/video/')[1].split('?')[0].split('_')[0];
    } else if (url.includes('dai.ly/')) {
      videoId = url.split('dai.ly/')[1].split('?')[0];
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `https://www.dailymotion.com/embed/video/${videoId}?autoplay=1`
    );
  }

  if (type === 'vimeo') {
    // https://vimeo.com/123456789 → 123456789
    const videoId = url.split('vimeo.com/')[1]?.split('?')[0];
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `https://player.vimeo.com/video/${videoId}?autoplay=1`
    );
  }

  // MP4 ou autre — pas d'embed, retourne l'URL directe
  return this.sanitizer.bypassSecurityTrustResourceUrl(url);
}

// Vérifie si c'est un embed (iframe) ou une vidéo directe (<video>)
isEmbed(url: string | undefined): boolean {
  const type = this.getVideoType(url);
  return type === 'youtube' || type === 'dailymotion' || type === 'vimeo';
}

// URL directe pour les fichiers MP4
getDirectVideoUrl(url: string): string {
  if (url.startsWith('http')) return url;
  return 'http://localhost:8080' + url;
}


  ngOnInit() {
    this.seriesId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadSeries();
    this.loadSeasons();
  }

  loadSeries() {
    this.movieService.getMovieById(this.seriesId).subscribe(data => this.series.set(data));
  }

  loadSeasons() {
    this.seasonService.getSeasonsBySeries(this.seriesId).subscribe(data => {
      this.seasons.set(data);
      // Sélectionner la première saison automatiquement
      if (data.length > 0) this.selectSeason(data[0]);
    });
  }

  selectSeason(season: Season) {
    this.selectedSeason.set(season);
    this.loadEpisodes(season.id!);
  }

  loadEpisodes(seasonId: number) {
    this.isLoadingEpisodes.set(true);
    this.episodeService.getEpisodesBySeason(seasonId).subscribe(data => {
      this.episodes.set(data);
      this.isLoadingEpisodes.set(false);
      // Charger la progression pour chaque épisode
      data.forEach(ep => this.loadEpisodeProgress(ep.id!));
    });
  }

  loadEpisodeProgress(episodeId: number) {
    this.watchProgressService.getEpisodeProgress(this.currentUserId, episodeId).subscribe(p => {
      this.progressMap.update(map => { const m = new Map(map); m.set(episodeId, p.progressPercent); return m; });
    });
  }

  getProgress(episodeId: number): number {
    return this.progressMap().get(episodeId) ?? 0;
  }

  playEpisode(episode: Episode) {
    this.activeEpisode.set(episode);
  }

  onEpisodeTimeUpdate(event: any, episodeId: number) {
    const video = event.target as HTMLVideoElement;
    if (video.duration > 0) {
      const percent = Math.round((video.currentTime / video.duration) * 100);
      const positionSeconds = Math.round(video.currentTime);
      // Sauvegarder toutes les 10 secondes
      if (positionSeconds % 10 === 0) {
        this.watchProgressService.saveEpisodeProgress(this.currentUserId, episodeId, percent, positionSeconds).subscribe();
        this.progressMap.update(map => { const m = new Map(map); m.set(episodeId, percent); return m; });
      }
    }
  }

  onAddSeason() {
    if (!this.seasonForm.valid) return;
    const data = {
      ...this.seasonForm.value,
      seriesId: this.seriesId
    };
    this.seasonService.createSeason(data as any).subscribe(saved => {
      this.seasons.update(prev => [...prev, saved]);
      this.seasonForm.reset();
      this.showAddSeason.set(false);
    });
  }

  onEpisodeFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file && file.size <= 1024 * 1024 * 1024) {
      this.selectedEpisodeFile = file;
    } else {
      alert('Fichier trop volumineux. Max 1 Go.');
      event.target.value = '';
    }
  }

  onAddEpisode() {
    if (!this.episodeForm.valid || !this.selectedEpisodeFile || !this.selectedSeason()) return;
    const data = {
      ...this.episodeForm.value,
      seasonId: this.selectedSeason()!.id
    };
    this.episodeService.createEpisode(data as any, this.selectedEpisodeFile).subscribe(saved => {
      this.episodes.update(prev => [...prev, saved]);
      this.episodeForm.reset();
      this.selectedEpisodeFile = null;
      this.showAddEpisode.set(false);
    });
  }

  deleteSeason(id: number) {
    if (!confirm('Supprimer cette saison et tous ses épisodes ?')) return;
    this.seasonService.deleteSeason(id).subscribe(() => {
      this.seasons.update(prev => prev.filter(s => s.id !== id));
      if (this.selectedSeason()?.id === id) {
        this.selectedSeason.set(null);
        this.episodes.set([]);
      }
    });
  }

  deleteEpisode(id: number) {
    if (!confirm('Supprimer cet épisode ?')) return;
    this.episodeService.deleteEpisode(id).subscribe(() => {
      this.episodes.update(prev => prev.filter(e => e.id !== id));
    });
  }
}
