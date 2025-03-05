import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from 'src/app/admin/admin.service';

@Component({
  selector: 'app-admin-approval',
  templateUrl: './admin-approval.component.html',
  styleUrls: ['./admin-approval.component.css']
})
export class AdminApprovalComponent implements OnInit {
  approvalForm!: FormGroup;
  pendingRequests: any[] = [];

  constructor(private fb: FormBuilder, private adminService: AdminService) {}

  ngOnInit(): void {
    this.approvalForm = this.fb.group({
      accountNumber: ['', Validators.required]
    });
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

  submitApproval(): void {
    if (this.approvalForm.valid) {
      const token = localStorage.getItem('jwtToken'); 
      if (token) {
        this.adminService.approveCardBlock(this.approvalForm.value, token).subscribe(
          (response: any) => {
            console.log('Card block status approved', response);
            alert('Successfully card block status has been approved by the admin');
            this.fetchPendingRequests(); 
          },
          (error: any) => {
            console.error('Approval failed', error);
            alert('Failed to approve card block status. Please try again.');
          }
        );
      } else {
        console.error('No JWT token found');
        alert('User authentication failed. Please log in again.');
      }
    }
  }

  approvePendingRequest(request: any): void {
    const token = localStorage.getItem('jwtToken'); 
    if (token) {
      this.adminService.approveCardBlock({ accountNumber: request.bankAccount.accountNumber }, token).subscribe(
        (response: any) => {
          console.log('Card block status approved', response);
          alert('Successfully card block status has been approved by the admin');
          this.fetchPendingRequests(); 
        },
        (error: any) => {
          console.error('Approval failed', error);
          alert('Failed to approve card block status. Please try again.');
        }
      );
    } else {
      console.error('No JWT token found');
      alert('User authentication failed. Please log in again.');
    }
  }
}
