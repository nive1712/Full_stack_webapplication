import { Component } from '@angular/core';
import { AtmService } from 'src/app/atm.service'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-withdraw',
  templateUrl: './withdraw.component.html',
  styleUrls: ['./withdraw.component.css']
})
export class WithdrawComponent {
  withdrawRequest = {
    accountNumber: '',
    amount: '',
    pin: ''
  };
  message: string | null = null; 
  messageType: 'success' | 'error' | null = null;

  constructor(private atmService: AtmService, private router: Router) {}

  onSubmit() {
    const token = localStorage.getItem('jwtToken');

    this.message = null;
    this.messageType = null;

    console.log('Withdraw Request Data:', this.withdrawRequest); 
    console.log('Token:', token); 

    this.atmService.withdraw(this.withdrawRequest, token || '').subscribe({
      next: (response: any) => {
        console.log('Withdraw Response:', response);
        this.message = response.message || 'Withdrawal successful!';
        this.messageType = 'success';
      },
      error: (error: { error: string; status: number }) => {
        console.error('Withdraw Error:', error); 
        this.message = error.error || 'Withdrawal failed. Please try again.';
        this.messageType = 'error'; 
      }
    });
  }
}
