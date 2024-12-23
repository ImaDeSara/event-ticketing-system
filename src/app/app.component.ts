import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { RouterModule } from '@angular/router';
import { ConfigurationFormComponent } from "./components/configuration-form/configuration-form.component";
import { ControlPanelComponent } from "./components/control-panel/control-panel.component";
import { LogDisplayComponent } from "./components/log-display/log-display.component";
import { TicketStatusComponent } from "./components/ticket-status/ticket-status.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, ConfigurationFormComponent, ControlPanelComponent, LogDisplayComponent, TicketStatusComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'event-ticketing-system-frontend';
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
  ],
};