import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NetBankingService {
  private baseUrl = 'http://localhost:8902/netbanking/process';

  constructor(private http: HttpClient) {}

  deposit(depositData: any, token: string): Observable<string> { 
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(this.baseUrl + '/deposit', depositData, { headers, responseType: 'text' }); // Added responseType: 'text'
  }

  transfer(transferData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(this.baseUrl + '/transfer', transferData,{ headers, responseType: 'text' });
  }
}
