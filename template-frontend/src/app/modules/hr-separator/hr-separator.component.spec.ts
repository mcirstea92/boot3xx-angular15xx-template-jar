import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HrSeparatorComponent } from './hr-separator.component';

describe('HrSeparatorComponent', () => {
  let component: HrSeparatorComponent;
  let fixture: ComponentFixture<HrSeparatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HrSeparatorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HrSeparatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
