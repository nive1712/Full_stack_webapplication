import { Component } from '@angular/core';
import { AtmService } from 'src/app/atm.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.css']
})
export class TransferComponent {
  transferRequest = {
    senderAccountNumber: '',
    recipientAccountNumber: '',
    amount: '',
    pin: ''
  };
  message: string | null = null;
  messageType: string = ''; 

  constructor(private atmService: AtmService, private router: Router) {}

  onSubmit() {
    const token = localStorage.getItem('jwtToken'); 

    console.log('Transfer Request Data:', this.transferRequest); 
    console.log('Token:', token); 

    this.atmService.transfer(this.transferRequest, token || '').subscribe({
      next: (response: any) => {
        console.log('Transfer Response:', response); 
        this.message = `Successfully transferred`;
        this.messageType = 'success'; 
        
      },
      error: (error: { error: string; }) => {
        console.error('Transfer Error:', error); 
        this.message = error.error || 'Transfer failed. Please try again.'; 
        this.messageType = 'error'; 
      }
    });
  }
}
