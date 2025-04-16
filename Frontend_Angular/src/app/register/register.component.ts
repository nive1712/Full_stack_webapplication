import { Component } from '@angular/core';
import { RegisterService } from '../register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user = {
    username: '',
    password: '',
    email: '',
    phoneNumber: '',
    role: 'USER'
  };

  constructor(private registerService: RegisterService) {}

  register(): void {
    this.registerService.register(this.user).subscribe({
      next: (response: any) => {
        console.log('Account created successfully', response);
        alert(`Thank you for creating your account with us !${JSON.stringify(response)}`);
      },
      error: (error: any) => {
        console.error('Error registering user', error);
      }
    });
  }
}

