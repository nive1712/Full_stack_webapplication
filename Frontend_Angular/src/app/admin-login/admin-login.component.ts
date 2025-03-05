import { Component } from '@angular/core';
import { AuthService } from '../auth.service'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.css']
})
export class AdminLoginComponent {
  credentials = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (token: string) => {
        console.log('Admin login successful, token:', token);
        localStorage.setItem('jwtToken', token);
        this.router.navigate(['/admin']); 
      },
      error: (error: any) => {
        console.error('Error during admin login', error);
      }
    });
  }
}
