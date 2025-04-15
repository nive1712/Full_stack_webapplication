import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetbankingTransferComponent } from './netbanking-transfer.component';

describe('NetbankingTransferComponent', () => {
  let component: NetbankingTransferComponent;
  let fixture: ComponentFixture<NetbankingTransferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NetbankingTransferComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetbankingTransferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
