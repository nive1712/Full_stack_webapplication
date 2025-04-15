import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardBlockService } from 'src/app/netbanking/card-block.service';

@Component({
  selector: 'app-card-block',
  templateUrl: './card-block.component.html',
  styleUrls: ['./card-block.component.css']
})
export class CardBlockComponent implements OnInit {
  cardBlockForm!: FormGroup;
  showSuccessGif = false;
  successMessage: string = '';

  constructor(private fb: FormBuilder, private cardBlockService: CardBlockService) {}

  ngOnInit(): void {
    this.cardBlockForm = this.fb.group({
      accountNumber: ['', Validators.required],
      pin: ['', [Validators.required, Validators.minLength(4)]],
      reason: ['', Validators.required]
    });
  }

  blockCard() {
    if (this.cardBlockForm.valid) {
      const token = localStorage.getItem('jwtToken'); 
      if (token) {
        const { accountNumber, pin, reason } = this.cardBlockForm.value;
        this.cardBlockService.blockCard(accountNumber, pin, reason, token).subscribe(
          (response: any) => {
            console.log('Card blocked successfully:', response);
          
            this.showSuccessGif = true;
            this.successMessage = 'Your card block request has been successfully submitted to the admin. We will address it at the earliest convenience.';
            
            setTimeout(() => {
              this.showSuccessGif = false;
              this.successMessage = '';
            }, 5000);
          },
          (error: any) => {
            console.error('Error blocking card:', error);
          }
        );
      } else {
        console.error('No JWT token found');
      }
    }
  }
}

