import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ApplicationEvent } from './notification.event.dto';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  private data: BehaviorSubject<StoreObj> = new BehaviorSubject<StoreObj>({
    isCustomer: false, isProcessOfficer: false, showHeaderItems: false, applicationId: '', shareCount: 0
  })
  currentData$: Observable<StoreObj> = this.data.asObservable();

  private notifications: BehaviorSubject<ApplicationEvent[]> = new BehaviorSubject<ApplicationEvent[]>([]);
  currentNotificationsData$: Observable<ApplicationEvent[]>  = this.notifications.asObservable();

  constructor() { }

  updateSharedData(keyName: StoreObjKey, value: boolean|string|number) {
    const currentValue = this.data.value

    const updated: StoreObj = {
      ...currentValue, 
      [keyName]: value
    }
    this.data.next(updated);
  }

  saveNotificationData(applicationEvents: ApplicationEvent[]) {
    this.notifications.next(applicationEvents);
  }

   setSharedDataToDefault() {
    this.data = new BehaviorSubject<StoreObj>({
      isCustomer: false, 
      isProcessOfficer: false,
      showHeaderItems: false,
      applicationId: '',
      shareCount: 0
    })
  }
}

export interface StoreObj {
  isCustomer: boolean, 
  isProcessOfficer: boolean,
  showHeaderItems: boolean,
  applicationId: string,
  shareCount: number
}

export enum StoreObjKey {
  IS_CUSTOMER = "isCustomer",
  IS_PROCESS_OFFICER = "isProcessOfficer",
  SHOW_HEADER_ITEMS = "showHeaderItems",
  APPLICATION_ID = "applicationId",
  SHARE_COUNT = "shareCount"
}
