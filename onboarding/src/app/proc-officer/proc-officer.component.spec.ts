import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcOfficerComponent } from './proc-officer.component';

describe('ProcOfficerComponent', () => {
  let component: ProcOfficerComponent;
  let fixture: ComponentFixture<ProcOfficerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcOfficerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcOfficerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
