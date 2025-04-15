import { Component } from '@angular/core';
import { NetBankingService } from 'src/app/netbanking.service';

@Component({
  selector: 'app-netbanking-transfer',
  templateUrl: './netbanking-transfer.component.html',
  styleUrls: ['./netbanking-transfer.component.css']
})
export class NetBankingTransferComponent {
  transferData = {
    senderAccountNumber: '',
    recipientAccountNumber: '',
    amount: null,
    pin: null
  };
  message: string = '';
  messageType: string = ''; 

  constructor(private netBankingService: NetBankingService) {}

  submitTransfer() {
    const token = localStorage.getItem('jwtToken'); 
    if (token) {
      this.netBankingService.transfer(this.transferData, token).subscribe((response: any) => {
        console.log('Transfer successful', response);
        this.message = 'Transfer successful';
        this.messageType = 'success';
      }, (error: any) => {
        console.error('Transfer failed', error);
        this.message = 'Transfer failed. Please try again later.';
        this.messageType = 'error'; 
      });
    } else {
      console.error('No JWT token found');
      this.message = 'Please login to initiate the transfer.';
      this.messageType = 'error'; 
    }
  }
}
