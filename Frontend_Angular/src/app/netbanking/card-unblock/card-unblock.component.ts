import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardBlockService } from 'src/app/netbanking/card-block.service'; // Adjust the path as necessary

@Component({
  selector: 'app-card-unblock',
  templateUrl: './card-unblock.component.html',
  styleUrls: ['./card-unblock.component.css']
})
export class CardUnblockComponent implements OnInit {
  cardUnblockForm!: FormGroup;
  message: string = '';

  constructor(private fb: FormBuilder, private cardBlockService: CardBlockService) {}

  ngOnInit(): void {
    this.cardUnblockForm = this.fb.group({
      accountNumber: ['', Validators.required],
      pin: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  unblockCard() {
    if (this.cardUnblockForm.valid) {
      const token = localStorage.getItem('jwtToken');
      console.log('JWT Token:', token);  
  
      if (token) {
        const { accountNumber, pin } = this.cardUnblockForm.value;
        console.log('Sending request with accountNumber:', accountNumber, 'and pin:', pin); 
  
        this.cardBlockService.unblockCard(accountNumber, Number(pin), token).subscribe(
          (response: any) => {
            this.message = 'Card unblocked successfully: ' + response;
            console.log('Response from backend:', response);  
          },
          (error: any) => {
            this.message = 'Error unblocking card: ' + error.error;
            console.error('Error from backend:', error.error);  
          }
        );
      } else {
        console.error('No JWT token found');
      }
    } else {
      console.log('Form is invalid');
    }
  }
}
