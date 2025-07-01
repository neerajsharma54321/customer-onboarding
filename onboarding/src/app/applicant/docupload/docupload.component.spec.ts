import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocuploadComponent } from './docupload.component';

describe('DocuploadComponent', () => {
  let component: DocuploadComponent;
  let fixture: ComponentFixture<DocuploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocuploadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocuploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
