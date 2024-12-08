import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlterComponent } from './alter.component';

describe('AlterComponent', () => {
  let component: AlterComponent;
  let fixture: ComponentFixture<AlterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
