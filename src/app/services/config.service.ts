import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  // config: {
  //   totalTickets: number;
  //   maxTicketCapacity: number;
  //   ticketReleaseRate: number;
  //   customerRetrievalRate: number;
  // } = {
  //   totalTickets: 50,
  //   maxTicketCapacity: 100,
  //   ticketReleaseRate: 10,
  //   customerRetrievalRate: 5,
  // };
  config = {
    totalTickets: 0,
    maxTicketCapacity: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0,
    releaseInterval: 0,
    retrievalInterval: 0,
    noOfVendors: 0,
    noOfCustomers: 0,
  };

  getConfig() {
    return this.config;
  }

  // setConfig(newConfig: {
  //   totalTickets: number;
  //   maxTicketCapacity: number;
  //   ticketReleaseRate: number;
  //   customerRetrievalRate: number;
  // }) {
  //   this.config = { ...this.config, ...newConfig };
  // }
  setConfig(newConfig: any) {
    this.config = { ...this.config, ...newConfig };
  }

  resetConfig() {
    this.config = {
      totalTickets: 0,
      maxTicketCapacity: 0,
      ticketReleaseRate: 0,
      customerRetrievalRate: 0,
      releaseInterval: 0,
      retrievalInterval: 0,
      noOfVendors: 0,
      noOfCustomers: 0,
    };
  }
}
