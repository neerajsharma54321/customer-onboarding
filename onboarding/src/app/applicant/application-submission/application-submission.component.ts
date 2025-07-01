import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedService } from 'src/app/util/shared.service';

@Component({
  selector: 'app-application-submission',
  templateUrl: './application-submission.component.html',
  styleUrls: ['./application-submission.component.scss']
})
export class ApplicationSubmissionComponent implements OnInit {

  applicationId?: string;
  constructor(private sharedService: SharedService, private router: Router) {
    this.sharedService.currentData$.subscribe(data => {
      if(data.applicationId) {
        this.applicationId = data.applicationId;
      } else {
        this.router.navigate(["/home"]);
      }
    });
   }

  ngOnInit(): void {

  }

  checkApplicationStatus() {
    this.router.navigate(["/application-status"], {state: {applicationId: this.applicationId}});
  }

}
