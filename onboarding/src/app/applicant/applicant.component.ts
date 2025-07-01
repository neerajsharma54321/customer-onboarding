import { Component, OnInit, ViewChild } from '@angular/core';
import { SharedService, StoreObjKey } from '../util/shared.service';
import { Router } from '@angular/router';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../util/auth.service';
import { RegistrationService } from '../util/registration.service';
import { ApiError } from '../util/error.response.dto';

@Component({
  selector: 'app-applicant',
  templateUrl: './applicant.component.html',
  styleUrls: ['./applicant.component.scss']
})
export class ApplicantComponent implements OnInit {

  constructor(private fb: FormBuilder, private sharedService: SharedService,
    private router: Router, private registrationService: RegistrationService) { }

  stepperForm!: FormGroup;
  step = 1;

  legalStructures = ['Limited', 'Trust', 'Fund'];
  industries = ['Finance', 'Real Estate', 'Services'];

  selectedFile: File | null = null;
  isDragging = false;

  showErrorMessage: boolean = false;
  apiError?: ApiError;

  @ViewChild('fileInput') fileInput!: any;

  ngOnInit() {
    // this.sharedService.updateSharedData(StoreObjKey.IS_CUSTOMER, true);
    this.stepperForm = this.fb.group({
      legalName: ['', Validators.required],
      legalStructure: ['', Validators.required],
      countryOfIncorporation: ['', Validators.required],
      registrationNumber: ['', Validators.required],
      taxId: [''],
      industryType: ['', Validators.required],
      directors: this.fb.array([this.createDirector()]),
      contactPerson: ['', Validators.required],
      contactEmail: ['', [Validators.required, Validators.email]],
      annualTurnover: [''],
      monthlyVolume: [''],
      ubos: this.fb.array([this.createUBO()]),
      documents: [null]
    });
  }

  get directors(): FormArray {
    return this.stepperForm.get('directors') as FormArray;
  }

  get ubos(): FormArray {
    return this.stepperForm.get('ubos') as FormArray;
  }

  createDirector(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      nationalId: ['', Validators.required],
      countryOfResidence: ['', Validators.required]
    });
  }

  createUBO(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      ownershipPercent: ['', Validators.required]
    });
  }


  addDirector() {
    this.directors.push(this.createDirector());
  }

  removeDirector(index: number) {
    this.directors.removeAt(index);
  }

  addUBO() {
    this.ubos.push(this.createUBO());
  }

  removeUBO(index: number) {
    this.ubos.removeAt(index);
  }

  onSubmit() {
    if (this.stepperForm.valid) {
      console.log('registration Data:', this.stepperForm.value);
      // call third party submission api
      var fileData: File = this.stepperForm.get('documents')?.value;
      this.registrationService.registerApplication(this.stepperForm.value, fileData).subscribe(
        next => {
          console.log("next data", next);
          var applicationId: string =  next.applicationId;
          this.sharedService.updateSharedData(StoreObjKey.APPLICATION_ID, applicationId)
          this.router.navigate(['/applicanttion-submission']);
        },
        error => {
          console.error("error data", error);
          this.showErrorMessage = true
          this.apiError = error;
        }
      )
      
    }
  }

  ngOnDestroy(): void {
    // this.sharedService.setSharedDataToDefault();
  }

  prevStep() {
    this.step -= 1;
  }

  nextStep() {
    this.step += 1;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      this.selectedFile = file;
      this.stepperForm.patchValue({ documents: file });
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = false;
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.setFile(event.dataTransfer.files[0]);
    }
  }

  setFile(file: File): void {
    const allowedTypes = ['image/jpeg', 'image/png', 'application/pdf'];
    const maxSize = 10 * 1024 * 1024; // 10 MB

    if (!allowedTypes.includes(file.type)) {
      this.showErrorMessage = true
      this.apiError = {message:'Invalid file type.'};
      return;
    }

    if (file.size > maxSize) {
      this.showErrorMessage = true
      this.apiError = {message:'File size exceeds 10MB.'};
      return;
    }
    this.selectedFile = file;
    this.stepperForm.patchValue({ documents: file });
  }

  clearFile(): void {
    this.selectedFile = null;
    this.stepperForm.get('documents')?.reset();
    if (this.fileInput) {
      this.fileInput.nativeElement.value = ''; // Reset the input
    }
  }
}
