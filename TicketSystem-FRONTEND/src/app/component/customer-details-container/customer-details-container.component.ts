import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Customer } from '../../models/customer/customer';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-details-container',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-details-container.component.html',
  styleUrl: './customer-details-container.component.css',
})
export class CustomerDetailsContainerComponent implements OnChanges {
  @Input() noCustomer: number = 0;
  customers: Customer[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['noCustomer']) {
      const perValue = changes['noCustomer'].previousValue;
      const currValue = changes['noCustomer'].currentValue;

      if (perValue > currValue) {
        this.customers.pop();
      } else if (perValue < currValue) {
        this.customers.push(new Customer());
      }
    }
  }
}
