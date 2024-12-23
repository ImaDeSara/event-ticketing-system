import { Component, OnInit } from '@angular/core';
import { TicketingService } from '../../services/ticketing.service';
import { FormsModule } from '@angular/forms';
import { NgFor } from '@angular/common';
import { ConfigService } from '../../services/config.service';
// Purpose: Accepts user inputs to configure parameters
@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [NgFor,FormsModule],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})

export class ConfigurationFormComponent implements OnInit {
  // Start with empty fields
  config = {
    totalTickets: 0,
    maxTicketCapacity: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0,
    releaseInterval: 0,
    retrievalInterval: 0,
    noOfVendors: 0,
    noOfCustomers: 0
  };

  constructor(
    private configService: ConfigService,
    private ticketingService: TicketingService
  ) {}

  // ngOnInit(): void {
  //   this.loadConfig(); // Load saved config when component initializes
  // }

  ngOnInit(): void {
    // this.loadConfig(); // Load saved config when component initializes
    this.syncWithService();
  }
  // Sync with ConfigService when reset occurs
  syncWithService(): void {
    this.config = this.configService.getConfig();
  }

  submitConfiguration(): void {
    if (this.isValidConfig()) {
      this.ticketingService.submitSystem(this.config).subscribe({
        next: (response) => {
          console.log('Submit response:', response);
          alert(response);
        },
        error: (err) => {
          console.error('Submit error:', err);
          alert(`Error: ${err.message}`);
        },
      });
    } else {
      alert('Please fill all the fields before submitting!');
    }
  }
  
  // Validate all fields are filled
  private isValidConfig(): boolean {
    return (
      this.config.totalTickets != null &&
      this.config.maxTicketCapacity != null &&
      this.config.ticketReleaseRate != null &&
      this.config.customerRetrievalRate != null &&
      this.config.releaseInterval != null &&
      this.config.retrievalInterval != null &&
      this.config.noOfVendors != null &&
      this.config.noOfCustomers != null 
    );
  }

  // Ensure that the saveConfig() method sends the config object (user-input data) correctly
  saveConfig(): void {
    this.ticketingService.saveConfig(this.config).subscribe({
      next: (response) => alert('Configuration saved: ' + response),
      error: (err) => console.error('Error saving configuration:', err),
    });
  }

  loadConfig(): void {
    this.ticketingService.loadConfig().subscribe({
      next: (loadedConfig) => {
        this.config = loadedConfig; // Update form fields with loaded config
        alert('Configuration loaded successfully!');
      },
      error: (err) => console.error('Error loading configuration:', err),
    });
  }

  resetConfig(): void {
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