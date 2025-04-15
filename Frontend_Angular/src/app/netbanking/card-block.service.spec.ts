import { TestBed } from '@angular/core/testing';

import { CardBlockService } from './card-block.service';

describe('CardBlockService', () => {
  let service: CardBlockService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardBlockService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

