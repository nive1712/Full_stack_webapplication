import { AtmService } from 'src/app/atm.service';
import { Router } from '@angular/router';
import { Component } from '@angular/core';

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent {
  depositData = {
    accountNumber: '',
    amount: '',
    pin: ''
  };

  message: string = ''; // To display success or error message
  messageType: string = ''; // To track message type (success/error)

  constructor(private atmService: AtmService, private router: Router) {}

  deposit(): void {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      this.atmService.deposit(this.depositData, token).subscribe({
        next: (response: any) => {
          console.log('Deposit successful:', response);
          this.message = `Successfully deposited`;
          this.messageType = 'success'; 
        },
        error: (error: any) => {
          console.error('Error during deposit', error);
          if (error.status === 403) {
            console.log("Your card is blocked. You cannot make deposits.");
            this.message = 'Your card is blocked. You cannot make deposits.';
            this.messageType = 'error'; 
          }
        }
      });
    } else {
      console.error('No JWT token found');
      this.message = 'Please login to make a deposit.';
      this.messageType = 'error';
    }
  }
}
