import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private baseUrl = 'http://localhost:8902/api/loan';

  constructor(private http: HttpClient) {}

  applyForLoan(loanData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.baseUrl}/apply`, loanData, { headers, responseType: 'text' });
  }

  payLoanDue(payLoanData: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post('http://localhost:8902/api/loandue/pay', payLoanData, { headers, responseType: 'text' }) ;
  }
}
