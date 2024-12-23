import { Component, OnInit, OnDestroy  } from '@angular/core';
import { TicketingService } from '../../services/ticketing.service';
import { Subscription, interval } from 'rxjs';
// Purpose: Displays the real-time ticket count
@Component({
  selector: 'app-ticket-status',
  standalone: true,
  imports: [],
  templateUrl: './ticket-status.component.html',
  styleUrl: './ticket-status.component.css'
})

export class TicketStatusComponent implements OnInit , OnDestroy {
  ticketCount: number = 0;
  private pollingSubscription!: Subscription;

  constructor(private ticketingService: TicketingService) {}
  
  ngOnDestroy(): void {
    // Unsubscribe to avoid memory leaks
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
  }

  // ngOnInit(): void {
  //   this.loadTicketCount();
  // }

  ngOnInit(): void {
    this.startPolling();
  }

  // Start polling the backend for ticket count every 5 seconds
  startPolling(): void {
    this.pollingSubscription = interval(1000).subscribe(() => {
      this.loadTicketCount();
    });
  }

  loadTicketCount(): void {
    this.ticketingService.getTicketCount().subscribe({
      next: (count) => {
        this.ticketCount = count;
      },
      error: (err) => {
        console.error('Error fetching ticket count:', err);
      }
    });
  }
}