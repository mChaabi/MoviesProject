import { TestBed } from '@angular/core/testing';

import { WatchProgress } from './watch-progress';

describe('WatchProgress', () => {
  let service: WatchProgress;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WatchProgress);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
