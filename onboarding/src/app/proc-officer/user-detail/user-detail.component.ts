import { Location } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { DirectorDto, FileInfo, RegistrationResponseDto, UboDto } from 'src/app/applicant/registrationResponseDto';
import { ApiError } from 'src/app/util/error.response.dto';
import { RegistrationService } from 'src/app/util/registration.service';
import { SharedService, StoreObjKey } from 'src/app/util/shared.service';


@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {
  stepperForm!: FormGroup;
  step = 1;

  legalStructures = ['Limited', 'Trust', 'Fund'];
  industries = ['Finance', 'Real Estate', 'Services'];

  selectedFile: File | null = null;
  isDragging = false;

  showErrorMessage: boolean = false;
  apiError?: ApiError;

  sanitizedUrl?: SafeResourceUrl;

  fileInfo?: FileInfo;
  isImage: boolean | any = null;

  dropdownData?: { key: string, label: string }[] = [];
  private dropdownDataList?: { key: string, label: string }[] = [
    { key: 'PENDING', label: 'PENDING' },
    { key: 'INITIATED', label: 'INITIATED' },
    { key: 'APPROVED', label: 'APPROVED' },
    { key: 'REJECTED', label: 'REJECTED' }
  ];

  applicationId: string = '';
  currentStatus: string = '';

  constructor(private location: Location, private fb: FormBuilder, private sharedService: SharedService,
    private router: Router, private registrationService: RegistrationService, private route: ActivatedRoute, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {

    this.route.queryParamMap.subscribe(params => {
      const appId = <string>params.get('applicationId');

      this.registrationService.findApplicantById(appId).subscribe(
        data => {

          var a = <RegistrationResponseDto>data;
          this.applicationId = a.applicationId;
          this.fileInfo = a.fileInfo;
          this.fileInfo.fileSize = this.fileInfo.fileSize / 1024;
          this.stepperForm = this.fb.group({
            legalName: [a.legalName, Validators.required],
            legalStructure: [a.legalStructure, Validators.required],
            countryOfIncorporation: [a.countryOfIncorporation, Validators.required],
            registrationNumber: [a.registrationNumber, Validators.required],
            taxId: [a.taxId, Validators.required],
            industryType: [a.industryType, Validators.required],
            directors: this.fb.array(this.createDirectors(a.directors)),
            contactPerson: [a.contactPerson, Validators.required],
            contactEmail: [a.contactEmail, [Validators.required, Validators.email]],
            annualTurnover: [a.annualTurnover, Validators.required],
            monthlyVolume: [a.monthlyVolume, Validators.required],
            ubos: this.fb.array(this.createUBO(a.ubos)),
            documents: this.fb.group({
              fileInfo: this.fb.group({
                fileName: [a.fileInfo.fileName],
                fileType: [a.fileInfo.fileType],
                fileSize: [a.fileInfo.fileSize],
                base64Content: [a.fileInfo.base64Content]
              })
            })
          });
          this.fileterDropdown(a.statusMessage);
          this.isImageOrPdf(this.fileInfo);
        },
        error => this.router.navigate(["processing-officer"])
      )
    });

  }

  private fileterDropdown(status: string) {
    this.currentStatus = status;
    if (this.currentStatus == 'PENDING') {
      this.dropdownData = [{ key: 'APPROVED', label: 'APPROVED' },
      { key: 'REJECTED', label: 'REJECTED' }]
    }
    else if (this.currentStatus == 'INITIATED') {
      this.dropdownData = [{ key: 'PENDING', label: 'PENDING' },
      { key: 'APPROVED', label: 'APPROVED' },
      { key: 'REJECTED', label: 'REJECTED' }]
    }
  }

  private isImageOrPdf(fileInfo: FileInfo) {
    if (fileInfo.fileType === 'application/pdf') {
      this.isImage = false;
      this.sanitizedUrl = this.sanitizer.bypassSecurityTrustResourceUrl('data:application/pdf;base64,' + fileInfo.base64Content);
    } else {
      this.isImage = true;
      this.sanitizedUrl = this.sanitizer.bypassSecurityTrustUrl('data:image/png;base64,' + fileInfo.base64Content);
    }
  }


  get directors(): FormArray {
    return this.stepperForm.get('directors') as FormArray;
  }

  get ubos(): FormArray {
    return this.stepperForm.get('ubos') as FormArray;
  }

  createDirectors(directors: DirectorDto[]): FormGroup[] {
    console.log('directors', directors);
    return directors.map(dir => this.fb.group({
      name: [dir.name, Validators.required],
      nationalId: [dir.nationalId, Validators.required],
      countryOfResidence: [dir.countryOfResidence, Validators.required]
    }));
  }

  createUBO(ubos: UboDto[]): FormGroup[] {
    console.log('ubos', ubos);
    return ubos.map(ubo => this.fb.group({
      name: [ubo.name, Validators.required],
      ownershipPercent: [ubo.ownershipPercent, Validators.required]
    }));
  }


  public back() {
    this.location.back();
  }

  prevStep() {
    this.step -= 1;
  }

  nextStep() {
    this.step += 1;
  }

  onSubmit() {
    if (this.stepperForm.valid) {
      console.log('registration Data:', this.stepperForm.value);
      // call third party submission api
      var fileData: File = this.stepperForm.get('documents')?.value;
      this.registrationService.registerApplication(this.stepperForm.value, fileData).subscribe(
        next => {
          console.log("next data", next);
          var applicationId: string = next.applicationId;
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

  updateStatus(status: string) {
    this.currentStatus = status;
  }

  changeStatus(status: string) {
    this.registrationService.updateApplicationStatusById(this.applicationId, status).subscribe(
      data => {
        this.sharedService.updateSharedData(StoreObjKey.APPLICATION_ID, this.applicationId);
        console.log("status updated successfully", data);
        this.router.navigate(["/processing-officer"]);
      },
      error => {
        console.error('error occured', error);
      }
    );
  }

}
