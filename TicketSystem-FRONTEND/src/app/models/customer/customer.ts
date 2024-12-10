export class Customer {
  customerId: string;
  name : string;
  noTicket: number;
  isVip: boolean;

  constructor(id : number) {
    this.customerId = `C-${id}`;
    this.name = `Customer ${id}`;
    this.noTicket = 0;
    this.isVip = false;
  }
}
