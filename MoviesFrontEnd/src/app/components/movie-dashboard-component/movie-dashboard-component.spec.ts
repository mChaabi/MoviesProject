import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieDashboardComponent } from './movie-dashboard-component';

describe('MovieDashboardComponent', () => {
  let component: MovieDashboardComponent;
  let fixture: ComponentFixture<MovieDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieDashboardComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
