import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import SockJS from 'sockjs-client';


@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket!: WebSocket;
  client : Client;
  private messageSubject = new Subject<string>(); // Subject to emit new messages
  message$ = this.messageSubject.asObservable();

  constructor() { 
    this.client = new Client()
    this.initializeConnection()
  }

  initializeConnection(){
    // this.client = new Client({
    //   webSocketFactory: () => new SockJS('http://localhost:8080/simulation-ws'), // Connect to the backend WebSocket
    //   onConnect: () => {
    //     console.log('WebSocket connection established!');
    //     // Subscribe to the "/topic/logs" topic after the connection is established
    //     this.client.subscribe('/topic/logs', (message) => {
    //       if (message) {
    //         console.log('Received message:', message.body); // Log the received message
    //         this.message.push(message.body); // Add message to the list
    //       }
    //     });
    //   },
    //   onDisconnect: () => {
    //     console.log('WebSocket connection closed!');
    //   },
    //   onStompError: (frame) => {
    //     console.error('STOMP error:', frame.headers['message']);
    //     console.error('Details:', frame.body);
    //   },
    //   debug: (str) => {
    //     console.log('STOMP Debug:', str); // Logs all STOMP activity
    //   }
    // });
    // this.client.activate();

    this.socket = new WebSocket('ws://localhost:8080/simulation-ws'); // Backend WebSocket URL

    this.socket.onopen = () => {
      console.log('WebSocket connection established!');
    };

    this.socket.onmessage = (event) => {
      this.messageSubject.next(event.data);
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed!');
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  }

  sendDataToBackend(data: string) {
    // if (this.client && this.client.connected) {
    //   // Send data to the backend on the /app/data destination
    //   this.client.publish({ destination: '/app/data', body: data });
    // } else {
    //   console.log('WebSocket is not connected');
    // }

    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(data); // Send data to the backend
      console.log('Data sent to backend:', data);
    } else {
      console.log('WebSocket is not connected');
    }
  }

}
