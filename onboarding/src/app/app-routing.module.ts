import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DocuploadComponent } from './applicant/docupload/docupload.component';
import { ApplicantComponent } from './applicant/applicant.component';
import { LoginComponent } from './login/login.component';
import { ProcOfficerComponent } from './proc-officer/proc-officer.component';
import { UserDetailComponent } from './proc-officer/user-detail/user-detail.component';
import { RoleGuardService } from './util/role-guard.service';
import { ApplicationStatusComponent } from './applicant/application-status/application-status.component';
import { ApplicationSubmissionComponent } from './applicant/application-submission/application-submission.component';

const routes: Routes = [
  {path: '', component: ApplicantComponent},
  {path: 'applicant', component: ApplicantComponent},
  {path: 'application-status', component: ApplicationStatusComponent},
  {path: 'applicanttion-submission', component: ApplicationSubmissionComponent},
  {path: 'login', component: LoginComponent},
  {path: 'processing-officer', component: ProcOfficerComponent, canActivate: [RoleGuardService]},
  {path: 'user-detail', component: UserDetailComponent, canActivate: [RoleGuardService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
