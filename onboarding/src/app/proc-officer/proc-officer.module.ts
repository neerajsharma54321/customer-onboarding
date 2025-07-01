import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProcOfficerComponent } from './proc-officer.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [ProcOfficerComponent, UserDetailComponent],
  imports: [
    CommonModule, ReactiveFormsModule
  ]
})
export class ProcOfficerModule { }
