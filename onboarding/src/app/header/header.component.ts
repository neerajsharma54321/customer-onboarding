import { Component, OnInit } from '@angular/core';
import { AuthService } from '../util/auth.service';
import { Router } from '@angular/router';
import { SharedService, StoreObjKey } from '../util/shared.service';
import { WebsocketService } from '../util/websocket.service';
import { ApplicationEvent } from '../util/notification.event.dto';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  menuItems: { label: string, path: string, icon?: string}[] =[]

  appNotificationData: ApplicationEvent[];

  constructor(public auth: AuthService, private router: Router, private sharedService: SharedService, private wsService: WebsocketService) { 
    this.appNotificationData = [];
  }

  ngOnInit(): void {
     this.wsService.getMessages().subscribe((data) => {
      this.appNotificationData.push(data);
      this.sharedService.saveNotificationData(this.appNotificationData);

      this.sharedService.currentData$.subscribe(data => {
        if(data.applicationId) {
          this.appNotificationData = this.appNotificationData.filter(app => app.applicationId != data.applicationId);
        }
      })
    });

    this.sharedService.currentData$.subscribe(item => {
      if(item.showHeaderItems == true){
        this.menuItems =[
        {label: "Home", path: "/processing-officer"}
      ]; 
      } else{
        this.menuItems = [
            {label: "Register applicant", path: "/applicant"},
        ]
      }
      this.menuItems.push({label: "Application status", path: "/application-status"});
    });

    if(this.auth.isLoggedIn()) {
       this.menuItems =[
        {label: "Home", path: "/processing-officer"}
      ]; 
    } else {
       this.menuItems =[
         {label: "Register applicant", path: "/applicant"}
      ]; 
    }
     this.menuItems.push({label: "Application status", path: "/application-status"});
  }

  logout() {
    this.auth.logout();
    this.sharedService.updateSharedData(StoreObjKey.SHOW_HEADER_ITEMS, false);
    this.router.navigate(["/login"]);
  }

  viewNewUser() {
    this.sharedService.saveNotificationData(this.appNotificationData);
    this.sharedService.updateSharedData(StoreObjKey.SHARE_COUNT, Math.random())
    this.router.navigate(["/processing-officer"]);
  }

}

