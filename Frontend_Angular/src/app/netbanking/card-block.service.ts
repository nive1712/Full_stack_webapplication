import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardBlockService {
  private apiUrl = 'http://localhost:8902/api/cards/block'; 
  private unblockApiUrl = 'http://localhost:8902/api/cards/unblock';
  constructor(private http: HttpClient) {}

  blockCard(accountNumber: string, pin: number, reason: string, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = {
      accountNumber,
      pin,
      reason
    };

    return this.http.post(this.apiUrl, body,{ headers, responseType: 'text' });
  }

    unblockCard(accountNumber: string, pin: number, token: string): Observable<any> {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      });
  
      const body = {
        accountNumber,
        pin
      };
  
      return this.http.post(this.unblockApiUrl, body, { headers, responseType: 'text' });
    }
}
