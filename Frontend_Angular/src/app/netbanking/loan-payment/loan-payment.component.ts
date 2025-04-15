import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoanService } from 'src/app/loan.service';

@Component({
  selector: 'app-loan-payment',
  templateUrl: './loan-payment.component.html',
  styleUrls: ['./loan-payment.component.css']
})
export class LoanPaymentComponent implements OnInit {
  loanPaymentForm!: FormGroup;

  constructor(private fb: FormBuilder, private loanService: LoanService) {}

  ngOnInit(): void {
    this.loanPaymentForm = this.fb.group({
      userId: ['', Validators.required],
      bankAccountId: ['', Validators.required],
      tenureYears: ['', Validators.required],
      confirmation: ['', Validators.required],
      accountNumber: ['', Validators.required]
    });
  }

  submitLoanPayment() {
    if (this.loanPaymentForm.valid) {
      const token = localStorage.getItem('jwtToken'); 
      if (token) {
        this.loanService.payLoanDue(this.loanPaymentForm.value, token).subscribe((response: any) => {
          console.log('Loan payment successful', response);
          alert('Payment successful');
        }, (error: any) => {
          console.error('Loan payment failed', error);
          if (error.error === 'You have already paid off the loan.') {
            alert('You have already paid off the loan.');
          } else {
            alert('Loan payment failed: ' + error.error);
          }
        });
      } else {
        console.error('No JWT token found');
      }
    }
  }
}

