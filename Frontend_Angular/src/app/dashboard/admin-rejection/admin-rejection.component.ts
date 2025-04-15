import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/admin/admin.service';

@Component({
  selector: 'app-admin-rejection',
  templateUrl: './admin-rejection.component.html',
  styleUrls: ['./admin-rejection.component.css']
})
export class AdminRejectionComponent implements OnInit {
  rejectionData = {
    accountNumber: ''
  };
  message: string | null = null;
  pendingRequests: any[] = [];

  constructor(private adminService: AdminService, private router: Router) {}

  ngOnInit(): void {
    this.fetchPendingRequests();
  }

  fetchPendingRequests(): void {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      this.adminService.getPendingCardBlockRequests(token).subscribe(
        (response: any) => {
          this.pendingRequests = response;
          console.log('Pending requests fetched', response);
        },
        (error: any) => {
          console.error('Error fetching pending requests', error);
        }
      );
    } else {
      console.error('No JWT token found');
    }
  }

  reject(): void {
    const token = localStorage.getItem('jwtToken');

    console.log('Rejection Request Data:', this.rejectionData);
    console.log('Token:', token);

    this.adminService.rejectCardBlockStatus(this.rejectionData, token || '').subscribe({
      next: (response: any) => {
        alert('Rejection Response: ' + response); 
        this.message = response;
        this.fetchPendingRequests();
        this.router.navigate(['/admin/reject']);
      },
      error: (error: { error: string; }) => {
        alert('Rejection Error: ' + error.error || 'Rejection failed. Please try again.'); 
        this.message = error.error || 'Rejection failed. Please try again.';
      }
    });
  }
}
