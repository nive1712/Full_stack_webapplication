import { TestBed } from '@angular/core/testing';

import { NetbankingService } from './netbanking.service';

describe('NetbankingService', () => {
  let service: NetbankingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetbankingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
