import { Configuration } from "../configuration/configuration";
import { Customer } from "../customer/customer";

export class LoggerData {
  config: Configuration | null;
  customers: Customer[];
  noCustomer: number;
  noVendor: number;
  isAutoGenCustomer: boolean;

  constructor(
    config: Configuration | null,
    customers: Customer[],
    noCustomer: number,
    noVendor: number,
    isAutoGenCustomer: boolean
  ) {
    this.config = config;
    this.customers = customers;
    this.noCustomer = noCustomer;
    this.noVendor = noVendor;
    this.isAutoGenCustomer = isAutoGenCustomer;
  }
}
