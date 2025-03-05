// src/app/admin.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  getAccountNumber(accountId: number, token: string) {
    throw new Error('Method not implemented.');
  }
  private baseUrl = 'http://localhost:8902/admin/card-block'; 

  constructor(private http: HttpClient) {}

  approveCardBlock(requestData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.baseUrl}/approve`, requestData, { headers, responseType: 'text' });
  }

  rejectCardBlockStatus(rejectionData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.baseUrl}/reject`, rejectionData, { headers, responseType: 'text' });
  }

  getPendingCardBlockRequests(token: string): Observable<any[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any[]>(`${this.baseUrl}/pending`, { headers });
  }
  
}

