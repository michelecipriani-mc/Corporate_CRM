import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeModal } from './home-modal';

describe('HomeModal', () => {
  let component: HomeModal;
  let fixture: ComponentFixture<HomeModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
