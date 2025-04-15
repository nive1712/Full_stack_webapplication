import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRejectionComponent } from './admin-rejection.component';

describe('AdminRejectionComponent', () => {
  let component: AdminRejectionComponent;
  let fixture: ComponentFixture<AdminRejectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminRejectionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRejectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
