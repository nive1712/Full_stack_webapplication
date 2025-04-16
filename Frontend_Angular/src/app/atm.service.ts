import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AtmService {
  private depositApiUrl = 'http://localhost:8902/atm/process/deposit';
  private withdrawApiUrl = 'http://localhost:8902/atm/process/withdraw';
  private transferApiUrl = 'http://localhost:8902/atm/process/transfer';

  constructor(private http: HttpClient) {}

  deposit(depositData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(this.depositApiUrl, depositData, { headers, responseType: 'text' });
  }

  withdraw(withdrawData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(this.withdrawApiUrl, withdrawData, { headers, responseType: 'text' });
  }

  transfer(transferData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(this.transferApiUrl, transferData, { headers, responseType: 'text' });
  }
}
