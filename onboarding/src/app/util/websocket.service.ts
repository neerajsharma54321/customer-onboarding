import { Injectable } from '@angular/core';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Subject, Observable } from 'rxjs';
import { ApplicationEvent } from './notification.event.dto';

@Injectable({ providedIn: 'root' })
export class WebsocketService {
  private stompClient: Client;
  private subject = new Subject<any>();

  constructor() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8093/ws'),
      reconnectDelay: 5000,
      debug: (str) => console.log(str)
    });

    // Set up connection and subscription
    this.stompClient.onConnect = (frame) => {
      this.stompClient.subscribe('/topic/applicants', (message: IMessage) => {
        this.subject.next(JSON.parse(message.body));
      });
    };

    this.stompClient.activate();
  }

  getMessages(): Observable<any> {
    return this.subject.asObservable();
  }
}
