import { CommonModule } from "@angular/common";
import { Component, inject, ViewChild } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CustomerDetailsContainerComponent } from "../../component/customer-details-container/customer-details-container.component";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApiServiceService } from "../../service/api/api-service.service";
import { Router } from "@angular/router";
import { Configuration } from "../../models/configuration/configuration";
import { LoggerData } from "../../models/loggerData/logger-data";

@Component({
  selector: "app-simulation",
  standalone: true,
  imports: [CommonModule, FormsModule, CustomerDetailsContainerComponent],
  templateUrl: "./simulation.component.html",
  styleUrl: "./simulation.component.css",
})
export class SimulationComponent {
  private _snackBar = inject(MatSnackBar);
  noOfCustomer: number = 0;
  noOfVendor: number = 0;
  isAutoGenCutomer: boolean = false;

  constructor(private api: ApiServiceService, private router: Router) {}

  @ViewChild(CustomerDetailsContainerComponent)
  customerDetailsContainer!: CustomerDetailsContainerComponent;

  changeNoCutomer(value: number) {
    this.noOfCustomer = value;
  }

  continueBtnAction() {
    // Check if the number of vendors is valid
    if (this.noOfVendor <= 0) {
      this.openSnackBar(
        "Number of vendor must be greater than 0",
        "close",
        "error"
      );
      return; 
    }
  
    // Check if the number of customers is valid
    if (this.noOfCustomer <= 0) {
      this.openSnackBar(
        "Number of customer must be greater than 0",
        "close",
        "error"
      );
      return; 
    }
  
    // Check individual customers if not auto-generating
    if (!this.isAutoGenCutomer) {
      for (const cus of this.customerDetailsContainer.customers) {
        if (!cus.customerId || cus.customerId.trim() === "") {
          this.openSnackBar("Please enter the customer ID", "close", "error");
          return; 
        }
  
        if (cus.noTicket <= 0) {
          this.openSnackBar(
            "Number of tickets must be greater than 0",
            "close",
            "error"
          );
          return; 
        }
      }
    }
  
    let data: LoggerData;
    if (this.isAutoGenCutomer) {
      data = new LoggerData(
        null, 
        [],
        this.noOfCustomer,
        this.noOfVendor,
        this.isAutoGenCutomer
      );
    } else {
      data = new LoggerData(
        null,
        this.customerDetailsContainer.customers,
        this.noOfCustomer,
        this.noOfVendor,
        this.isAutoGenCutomer
      );
    }
  
    // Store data in localStorage
    localStorage.setItem("credentialData", JSON.stringify(data));
    this.router.navigate(["/logger"]);
  }

  openSnackBar(message: string, action: string, type: "error" | "success") {
    const snackBarConfig = {
      duration: 5000,
      panelClass: type == "error" ? "snack-bar-error" : "snack-bar-success",
    };
    this._snackBar.open(message, action, snackBarConfig);
  }
}
