import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardUnblockComponent } from './card-unblock.component';

describe('CardUnblockComponent', () => {
  let component: CardUnblockComponent;
  let fixture: ComponentFixture<CardUnblockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CardUnblockComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardUnblockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

