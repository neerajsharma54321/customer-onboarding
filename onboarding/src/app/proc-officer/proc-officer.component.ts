import { Component, OnInit } from '@angular/core';
import { SharedService, StoreObjKey } from '../util/shared.service';
import { Router } from '@angular/router';
import { RegistrationService } from '../util/registration.service';
import { ApplicationLeastResponseDto } from '../applicant/registrationResponseDto';
import { WebsocketService } from '../util/websocket.service';
import { ApplicationEvent } from '../util/notification.event.dto';

@Component({
  selector: 'app-proc-officer',
  templateUrl: './proc-officer.component.html',
  styleUrls: ['./proc-officer.component.scss']
})
export class ProcOfficerComponent implements OnInit {

  applications?: ApplicationLeastResponseDto[];
  typeOfApplicationsChoice?: { label: string, key: 'newApp' | 'all' | 'pending' }[];
  selectedAppChoice: 'pending' | 'all' | 'newApp' = 'pending';
  isLoading: boolean = true;

  constructor(private sharedService: SharedService, private router: Router, private registrationService: RegistrationService) {
    this.typeOfApplicationsChoice = [
      { label: 'All Applications', key: 'all' },
      { label: 'Pending Applications', key: 'pending' },
      { label: 'New registrations', key: 'newApp' }
    ]

  }

  ngOnInit(): void {

    this.sharedService.currentData$.subscribe(data => {
      this.selectedAppChoice = 'newApp';
      if (this.selectedAppChoice == 'newApp') {
        this.isLoading = false;
        if(data.applicationId) {
          this.applications = this.applications?.filter(app => app.applicationId != data.applicationId);
          this.sharedService.saveNotificationData(<ApplicationEvent[]>this.applications);
        }
        this.sharedService.currentNotificationsData$.subscribe(data => {
          this.applications = data;
        })
      } else {
        this.findApplicationByChoice();
      }
    });
  }

  public showDetail(id: string) {
    this.router.navigate(['/user-detail'], { queryParams: { applicationId: id }, fragment: 'detail' })
  }

  public getApplications(selectedChoice: 'newApp' | 'all' | 'pending') {
    this.selectedAppChoice = selectedChoice;
    this.findApplicationByChoice();
  }

  private findApplicationByChoice() {
    var params = this.selectedAppChoice == 'all' ? ['INITIATED', 'PENDING', 'APPROVED', 'REJECTED'] : this.selectedAppChoice === 'pending' ? ['PENDING'] : [];

    if (params.length == 0) {
      this.sharedService.currentNotificationsData$.subscribe(data => {
        this.applications = data;
      })
    } else {
      this.registrationService.findApplicationsByStatus(params).subscribe(
        data => {
          this.applications = data;
        },
        error => console.error("An error found", error),
        () => this.isLoading = false
      );
    }
  }
}

