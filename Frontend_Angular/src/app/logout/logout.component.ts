import { Component } from '@angular/core';
import { AuthService } from '../auth.service'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {
  loading: boolean = false; 
  logoutMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  logout(): void {
    this.loading = true;
    this.logoutMessage = "Logging out..."; 

    localStorage.removeItem('jwtToken');
    console.log('JWT Token removed from localStorage');

    setTimeout(() => {
      this.logoutMessage = "You have been logged out successfully.";
      this.router.navigate(['/login']);
    }, 2000); 
  }

  ngOnInit(): void {
    this.logout();
  }
}
