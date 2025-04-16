import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BudgetService {
  private apiUrl = 'http://localhost:8902/netbanking/process'; 

  constructor(private http: HttpClient) {}

  calculateBudget(
    totalIncome: number,
    totalExpenses: number,
    debtRepayment: number,
    token: string
  ): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = {
      totalIncome,
      totalExpenses,
      debtRepayment
    };

    return this.http.post(`${this.apiUrl}/calculate`, body, { headers, responseType: 'text' });
  }
}
