import { Component, OnInit, OnDestroy  } from '@angular/core';
import { TicketingService } from '../../services/ticketing.service';
import { CommonModule } from '@angular/common';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './log-display.component.html',
  styleUrl: './log-display.component.css'
})

export class LogDisplayComponent implements OnInit {
  logs: string[] = []; //Store logs here
  private pollingSubscription!: Subscription;

  constructor(private ticketingService: TicketingService) {}

  ngOnInit(): void {
    this.startPolling();
  }

  startPolling(): void {
    this.pollingSubscription = interval(2000).subscribe(() => {
      this.fetchLogs();
    });
  }

  fetchLogs(): void {
    this.ticketingService.getLogs().subscribe({
      next: (logData) => {
        this.logs = logData;
      },
      error: (err) => {
        console.error('Error fetching logs:', err);
      }
    });
  }
}
