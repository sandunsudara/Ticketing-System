import { CommonModule } from '@angular/common';
import {
  AfterViewChecked,
  Component,
  inject,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ApiService } from '../../service/api/api.service';
import { Configuration } from '../../models/configuration/configuration';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoggerData } from '../../models/loggerData/logger-data';
import { WebSocketService } from '../../service/ws/web-socket.service';
import { Customer } from '../../models/customer/customer';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-logger',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './logger.component.html',
  styleUrl: './logger.component.css',
})
export class LoggerComponent implements OnInit, OnDestroy, AfterViewChecked {
  loggerData!: LoggerData;
  newCustomer: Customer = new Customer(0);
  private _snackBar = inject(MatSnackBar);
  @ViewChild('logContent') logContent: any;
  isStart: boolean = true;
  logs: string[] = [];

  constructor(
    private api: ApiService,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    const storedData = localStorage.getItem('credentialData');
    if (storedData) {
      this.loggerData = JSON.parse(storedData) as LoggerData;
      this.api.get<Configuration>('system/get-config').subscribe((res) => {
        if (res.success) {
          this.loggerData.config = res.data;
        } else {
          this.openSnackBar(res.message, 'close', 'error');
        }
      });
    } else {
      console.log('No state or local storage data found.');
    }

    this.webSocketService.client.onConnect = () => {
      console.log('WebSocket connection established in Angular!');
    };

    this.webSocketService.message$.subscribe((message) => {
      this.logs.push(message);
    });
  }

  ngOnDestroy(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('credentialData');
    }
  }

  ngAfterViewChecked(): void {
    // Scroll to the bottom of the log content after every update
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    // Scroll the log content container to the bottom
    const logContent = this.logContent.nativeElement;
    logContent.scrollTop = logContent.scrollHeight;
  }

  startBtnAction() {
    if (this.loggerData == null) {
      this.openSnackBar(
        'Confuigration and credential are not initialize',
        'close',
        'error'
      );
    } else {
      this.logs = [];
      if (!this.webSocketService.client.active) {
        if (this.loggerData.isAutoGenCustomer) {
          this.api
            .post<null>(
              `system/start-genCustomer?noCustomer=${this.loggerData.noCustomer}&noVendor=${this.loggerData.noVendor}`,
              null
            )
            .subscribe((res) => {
              if (res.success) {
                this.openSnackBar(res.message, 'close', 'success');
                this.isStart = !this.isStart;
              } else {
                this.openSnackBar(res.message, 'close', 'error');
              }
            });
        } else {
          console.log(this.loggerData.customers);
          this.api
            .post<null>(
              `system/start-with-customer?noVendor=${this.loggerData.noVendor}`,
              this.loggerData.customers
            )
            .subscribe((res) => {
              if (res.success) {
                this.openSnackBar(res.message, 'close', 'success');
                this.isStart = !this.isStart;
              } else {
                this.openSnackBar(res.message, 'close', 'error');
              }
            });
        }
      } else {
        this.openSnackBar(
          'Web socket not cconteced, please reload page',
          'close',
          'error'
        );
      }
    }
  }

  stopBtnAction() {
    this.api.get('system/stop-simulation').subscribe(res =>{
      if(res.success){
        this.openSnackBar(res.message, 'close', 'success');
        this.isStart = !this.isStart
      }
      else{
        this.openSnackBar(res.message, 'close', 'error');
      }
    });
  }

  addVendorBtnAction() {
    if (this.isStart) {
      this.openSnackBar('Simulation not start yet', 'close', 'error');
    } else {
      this.api.put('system/add-vendor', null).subscribe((res) => {
        if (res.success) {
          this.openSnackBar(res.message, 'close', 'success');
          this.updateCredential();
        } else {
          this.openSnackBar(res.message, 'close', 'error');
        }
      });
    }
  }

  addCustomerBtnAction() {
    if (this.isStart) {
      this.openSnackBar('Simulation not start yet', 'close', 'error');
    } else {
      if (this.newCustomer.numberOfTicket <= 0) {
        this.openSnackBar(
          'Number of ticket must be great than 0',
          'close',
          'error'
        );
      }
      console.log(this.newCustomer);
      this.api
        .put<null>('system/add-customer', this.newCustomer)
        .subscribe((res) => {
          if (res.success) {
            this.openSnackBar(res.message, 'close', 'success');
            this.updateCredential();
          } else {
            this.openSnackBar(res.message, 'close', 'error');
          }
        });
    }
  }

  updateCredential() {
    this.api.get('system/get-credential').subscribe((res) => {
      if (res.success) {
        const data = (res as any).data;
        this.loggerData.noCustomer = data?.customerThreadLength ?? 0;
        this.loggerData.noVendor = data?.vendorThreadLength ?? 0;
      } else {
        console.log('fuck');
      }
    });
  }

  openSnackBar(message: string, action: string, type: 'error' | 'success') {
    const snackBarConfig = {
      duration: 5000,
      panelClass: type == 'error' ? 'snack-bar-error' : 'snack-bar-success',
    };
    this._snackBar.open(message, action, snackBarConfig);
  }
}
