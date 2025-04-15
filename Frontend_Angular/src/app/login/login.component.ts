import { Component } from '@angular/core';
import { AuthService } from '../auth.service'; // Ensure you have this service
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (token: string) => {
        console.log('Login successful, token:', token);
        localStorage.setItem('jwtToken', token);
        this.router.navigate(['/dashboard']); 
      },
      error: (error: any) => {
        console.error('Error during login', error);
      }
    });
  }
}
