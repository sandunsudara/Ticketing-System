import { CommonModule } from "@angular/common";
import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { ApiServiceService } from "../../service/api/api-service.service";
import { Configuration } from "../../models/configuration/configuration";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";
import { LoggerData } from "../../models/loggerData/logger-data";

@Component({
  selector: "app-logger",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./logger.component.html",
  styleUrl: "./logger.component.css",
})
export class LoggerComponent implements OnInit, OnDestroy {
  loggerData!: LoggerData;

  private _snackBar = inject(MatSnackBar);
  isStart: boolean = true;

  constructor(private api: ApiServiceService, private router: Router) {}

  ngOnInit(): void {
    const storedData = localStorage.getItem("credentialData");
    if (storedData) {
      this.loggerData = JSON.parse(storedData) as LoggerData;
      this.api.get<Configuration>("system/get-config").subscribe((res) => {
        if (res.success) {
          this.loggerData.config = res.data;
        } else {
          this.openSnackBar(res.message, "close", "error");
        }
      });
    } else {
      console.log("No state or local storage data found.");
    }
  }

  ngOnDestroy(): void {
    if (typeof localStorage !== "undefined") {
      localStorage.removeItem("credentialData");
    }
  }

  startBtnAction() {
    this.isStart = !this.isStart;
    if (this.loggerData == null) {
      this.openSnackBar(
        "Confuigration and credential are not initialize",
        "close",
        "error"
      );
    }
    else{
      if(this.loggerData.isAutoGenCustomer){
        this.api.post(`system/start-genCustomer?noCustomer=${this.loggerData.noCustomer}&noVendor=${this.loggerData.noVendor}`,null).subscribe();
      }
      else{
        this.api.post(`system/start-with-customer?noVendor=${this.loggerData.noVendor}`,this.loggerData.customers).subscribe();
      }
      
    }
  }
  stopBtnAction() {
    this.isStart = !this.isStart;
  }

  openSnackBar(message: string, action: string, type: "error" | "success") {
    const snackBarConfig = {
      duration: 5000,
      panelClass: type == "error" ? "snack-bar-error" : "snack-bar-success",
    };
    this._snackBar.open(message, action, snackBarConfig);
  }
}
