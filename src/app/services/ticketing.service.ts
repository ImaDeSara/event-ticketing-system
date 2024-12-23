import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// A service is to handle API calls
@Injectable({
  providedIn: 'root',
})
export class TicketingService {
  private apiUrl = 'http://localhost:8080/api/tickets';

  constructor(private http: HttpClient) {}

  submitSystem(payload: any): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/submit`, payload, { responseType: 'text' as 'json' });
  }

  startSystem(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/start`, {}, { responseType: 'text' as 'json' });
  }
  
  stopSystem(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/stop`, {}, { responseType: 'text' as 'json' });
  }
  
  getTicketCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
  // Fetch logs as an array of strings
  getLogs(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/logs`);
  }

  //Ensure the resetSystem method in TicketingService specifies the expected response type as text
  resetSystem(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/reset`, {}, { responseType: 'text' as 'json' });
  }
   
  saveConfig(config: any): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/saveConfig`, config, { responseType: 'text' as 'json' }); //Angular expects and handles plain text responses from the backend
  }
  
  loadConfig(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/loadConfig`);
  }
  
}

