export class Customer {
  customerId: string;
  name : string;
  numberOfTicket: number;
  isVip: boolean;

  constructor(id : number) {
    this.customerId = `C-${id}`;
    this.name = `Customer ${id}`;
    this.numberOfTicket = 0;
    this.isVip = false;
  }
}
