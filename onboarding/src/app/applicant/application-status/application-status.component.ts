import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiError } from 'src/app/util/error.response.dto';
import { RegistrationService } from 'src/app/util/registration.service';
import { SharedService } from 'src/app/util/shared.service';

@Component({
  selector: 'app-application-status',
  templateUrl: './application-status.component.html',
  styleUrls: ['./application-status.component.scss']
})
export class ApplicationStatusComponent implements OnInit {

  showMessage?: boolean = false;
  applicationId?: string = '';
  appStatusForm!: FormGroup;
  appStatus?: string;
  messageClass?: string;

  constructor(private router: Router, private fb: FormBuilder, private registrationService: RegistrationService) {
    const navigation = this.router.getCurrentNavigation();
    var appId = navigation?.extras?.state?.['applicationId'];
    if (appId) {
      this.applicationId = appId;
    }
  }

  ngOnInit(): void {
    this.appStatusForm = this.fb.group({
      appId: [this.applicationId, Validators.required]
    });
  }


  checkStatus() {
    if (this.appStatusForm.valid) {
      // Get specific field value
      const applicationId = this.appStatusForm.get('appId')?.value;
      this.registrationService.findApplicationStatusById(applicationId).subscribe(
        data => {
          this.appStatus = `Application status: <b>${data}</b>`;
          this.messageClass = 'alert alert-success';
        },
        (error: ApiError) => {
          this.appStatus = `<b>${error.message}</b>`;
          this.messageClass = 'alert alert-danger';
        },
        () => this.showMessage = true
      )
    }

  }
}
