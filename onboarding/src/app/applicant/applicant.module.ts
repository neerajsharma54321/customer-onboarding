import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicantComponent } from './applicant.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ApplicationSubmissionComponent } from './application-submission/application-submission.component';
import { ApplicationStatusComponent } from './application-status/application-status.component';



@NgModule({
  declarations: [
    ApplicantComponent,
    ApplicationSubmissionComponent,
    ApplicationStatusComponent
  ],
  imports: [
    CommonModule, ReactiveFormsModule
  ]
})
export class ApplicantModule { }
