import { CommonModule } from '@angular/common';
import { Component,  } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CustomerDetailsContainerComponent } from '../../component/customer-details-container/customer-details-container.component';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomerDetailsContainerComponent],
  templateUrl: './simulation.component.html',
  styleUrl: './simulation.component.css',
})
export class SimulationComponent  {
  noOfCustomer: number = 0;
  noOfVendor: number = 0;
  isAutoGenCutomer: boolean = false;


  changeNoCutomer(value : number){
    this.noOfCustomer = value
  }

 
  
}
