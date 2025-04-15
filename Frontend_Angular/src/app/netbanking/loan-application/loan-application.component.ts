import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoanService } from 'src/app/loan.service';

@Component({
  selector: 'app-loan-application',
  templateUrl: './loan-application.component.html',
  styleUrls: ['./loan-application.component.css']
})
export class LoanApplicationComponent implements OnInit {
  loanForm!: FormGroup;
  message: string = ''; 
  messageType: string = ''; 

  constructor(private fb: FormBuilder, private loanService: LoanService) {}

  ngOnInit(): void {
    this.loanForm = this.fb.group({
      bankAccountId: ['', Validators.required],
      totalPayment: ['', Validators.required],
      downPayment: ['', Validators.required],
      tenureYears: ['', Validators.required],
      loanType: ['', Validators.required],
      accountNumber: ['', Validators.required] 
    });
  }

  submitLoanApplication() {
    if (this.loanForm.valid) {
      const token = localStorage.getItem('jwtToken'); 
      if (token) {
        this.loanService.applyForLoan(this.loanForm.value, token).subscribe((response: any) => {
          console.log('Loan application successful', response);
          this.message = 'Loan application submitted successfully';
          this.messageType = 'success'; 
        }, (error: any) => {
          console.error('Loan application failed', error);
          this.message = 'Loan application failed. Please try again later.';
          this.messageType = 'error'; 
        });
      } else {
        console.error('No JWT token found');
        this.message = 'Please login to apply for a loan.';
        this.messageType = 'error'; 
      }
    }
  }
}
