import { Routes } from '@angular/router';
import { ConfigurationFormComponent } from './components/configuration-form/configuration-form.component';
import { TicketStatusComponent } from './components/ticket-status/ticket-status.component';
import { ControlPanelComponent } from './components/control-panel/control-panel.component';
import { LogDisplayComponent } from './components/log-display/log-display.component';

export const routes: Routes = [
  { path: '', redirectTo: '/ticket-status', pathMatch: 'full' }, // Default route
  { path: 'configuration', component: ConfigurationFormComponent },
  { path: 'ticket-status', component: TicketStatusComponent },
  { path: 'control-panel', component: ControlPanelComponent },
  { path: 'logs', component: LogDisplayComponent },
];
