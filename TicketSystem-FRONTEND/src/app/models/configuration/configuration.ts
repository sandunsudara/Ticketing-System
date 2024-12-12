export class Configuration {
  totalTickets: number;
  ticketReleaseRate: number;
  customerRetrievalRate: number;
  maxTicketCapacity: number;

  constructor(
    totalTickets: number,
    ticketReleaseRate: number,
    customerRetrievalRate: number,
    maxTicketCapacity: number
  ) {
    this.totalTickets = totalTickets;
    this.ticketReleaseRate = ticketReleaseRate;
    this.customerRetrievalRate = customerRetrievalRate;
    this.maxTicketCapacity = maxTicketCapacity;
  }
}
