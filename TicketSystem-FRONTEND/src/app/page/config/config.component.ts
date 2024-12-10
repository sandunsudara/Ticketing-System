import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Configuration } from '../../models/configuration/configuration';
import { FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ApiServiceService } from '../../service/api/api-service.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Response } from '../../models/response/response';

@Component({
  selector: 'app-config',
  standalone: true,
  imports: [CommonModule, FormsModule, MatSnackBarModule],
  templateUrl: './config.component.html',
  styleUrl: './config.component.css',
})
export class ConfigComponent {
  config: Configuration = new Configuration(0,0,0,0);
  private _snackBar = inject(MatSnackBar);

  constructor(private api: ApiServiceService, private http: HttpClient) {}

  submitForm() {
    if (this.config.totalTickets <= 0) {
      this.openSnackBar(
        'Total tickets must be greater than 0',
        'close',
        'error'
      );
      return;
    }
    if (this.config.ticketReleaseRate <= 0) {
      this.openSnackBar(
        'Ticket release rate must be greater than 0',
        'close',
        'error'
      );
      return;
    }
    if (this.config.customerRetrievalRate <= 0) {
      this.openSnackBar(
        'Customer retrieval rate must be greater than 0',
        'close',
        'error'
      );
      return;
    }
    if (this.config.maxTicketCapacity <= 0) {
      this.openSnackBar(
        'Max ticket capacity must be greater than 0',
        'close',
        'error'
      );
      return;
    }

    this.api.post<Response<null>>('system/save-config', this.config).subscribe((res) => {
      console.log(res)
      if (res.success) {
        this.openSnackBar(res.message, 'close', 'success');
        this.config = new Configuration(0,0,0,0);

      } else {
        console.log("sasa")
        this.openSnackBar(res.message, 'close', 'error');
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
