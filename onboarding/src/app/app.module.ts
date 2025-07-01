import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { ProcOfficerComponent } from './proc-officer/proc-officer.component';
import { Route } from '@angular/router';
import { DocuploadComponent } from './applicant/docupload/docupload.component';
import { ApplicantComponent } from './applicant/applicant.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { ProcOfficerModule } from './proc-officer/proc-officer.module';
import { HttpClientModule } from '@angular/common/http';
import { ApplicantModule } from './applicant/applicant.module';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule, 
    AppRoutingModule,
    FormsModule, ReactiveFormsModule, HttpClientModule, ProcOfficerModule, ApplicantModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
