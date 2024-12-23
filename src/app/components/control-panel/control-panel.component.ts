import { Component } from '@angular/core';
import { TicketingService } from '../../services/ticketing.service';
import { ConfigService } from '../../services/config.service';

// Purpose: Provides start, stop, and reset controls
@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})

export class ControlPanelComponent {
  currentConfig: any = null; // Store the last saved configuration
  config!: { totalTickets: number; maxTicketCapacity: number; ticketReleaseRate: number; customerRetrievalRate: number; };

  constructor(
    private configService: ConfigService,
    private ticketingService: TicketingService
  ) {}

  startSystem(): void {
    this.ticketingService.startSystem().subscribe({
      next: (response) => {
        alert(response);
        console.log('System started successfully:', response);
      },
      error: (err) => {
        console.error('Error starting the system:', err);
        alert(`Failed to start system: ${err.message}`);
      },
    });
  }

  stopSystem(): void {
    this.ticketingService.stopSystem().subscribe({
      next: (response) => {
        alert(response);
        console.log('System stopped successfully:', response);
      },
      error: (err) => {
        console.error('Error stopping the system:', err);
        alert(`Failed to stop system: ${err.message}`);
      },
    });
  }
  
  // resetSystem(): void {
  //   this.ticketingService.resetSystem().subscribe({
  //     next: (response) => {
  //       alert(response); // Show success message
  //       this.configService.resetConfig(); // Reset configuration in the service
  //     },
  //     error: (err) => {
  //       console.error('Error resetting system:', err.message || err);
  //       alert('Failed to reset the system. Check the console for details.');
  //     },
  //   });
  // }
 
  resetSystem(): void {
    this.ticketingService.resetSystem().subscribe({
      next: (response) => {
        alert(response); // Show success message
        this.configService.resetConfig(); // Reset configuration in the service
      },
      error: (err) => {
        console.error('Error resetting system:', err.message || err);
        alert('Failed to reset the system. Check the console for details.');
      },
    });
  }
  

  saveConfig(): void {
    this.ticketingService.saveConfig(this.configService.getConfig()).subscribe({ // Get the updated config
      next: (response) => alert('Configuration saved: ' + response),
      error: (err) => console.error('Error saving configuration:', err),
    });
  }

  loadConfig(): void {
    this.ticketingService.loadConfig().subscribe({
      next: (config: any) => {
        this.configService.setConfig(config);
        alert('Configuration loaded');
      },
      error: (err: any) => console.error(err), // Explicitly type 'err' as 'any'
    });
  }
  
  updateConfig() {
    this.configService.setConfig(this.config); // Update shared configuration
  }
}
