import { Component } from '@angular/core';
import { NetBankingService } from 'src/app/netbanking.service';

declare var Razorpay: any;

@Component({
  selector: 'app-netbanking-deposit',
  templateUrl: './netbanking-deposit.component.html',
  styleUrls: ['./netbanking-deposit.component.css']
})
export class NetBankingDepositComponent {
  depositData = {
    accountNumber: '',
    amount: '',
    pin: '',
  };

  constructor(private netBankingService: NetBankingService) {}

  submitDeposit(): void {
    this.initiatePayment(parseFloat(this.depositData.amount));
  }

  initiatePayment(amount: number): void {
    const script = document.createElement('script');
    script.src = 'https://checkout.razorpay.com/v1/checkout.js';
    script.async = true;
    document.body.appendChild(script);

    script.onload = () => {
      const options = {
        key: 'rzp_test_MRKC295FvSdslC', 
        amount: amount * 100, 
        currency: 'INR',
        name: 'Virtusa Bank of India',
        description: 'Deposit Transaction',
        handler: (response: any) => {
          console.log('Razorpay payment ID:', response.razorpay_payment_id);
          
          if (response.razorpay_payment_id) {
            alert(`Payment successful with ID: ${response.razorpay_payment_id}`);
            this.finalizeDeposit(response.razorpay_payment_id);
          } else {
            console.error('Payment failed: No payment ID received');
            alert('Oops, something went wrong. Payment failed.');
          }
        },
        prefill: {
          contact: '9001009090' 
        },
        theme: {
          color: '#3399cc'
        },

      };

      const rzp = new Razorpay(options);
      rzp.open();
    };
  }

  finalizeDeposit(paymentId: string): void {
    console.log('Finalizing deposit with payment ID:', paymentId);
    const token = localStorage.getItem('jwtToken');
    if (token) {
      this.netBankingService.deposit(this.depositData, token).subscribe({
        next: (response: any) => {
          console.log('Deposit successful:', response);
          alert('Deposit successful: ' + response);
        },
        error: (error: any) => {
          console.error('Error during deposit', error);
          if (error.status === 403) {
            console.log("Your card is blocked. You cannot make deposits.");
            alert('Your card is blocked. You cannot make deposits.');
          }
        }
      });
    } else {
      console.error('No JWT token found');
      alert('Please login to make a deposit.');
    }
  }
}
