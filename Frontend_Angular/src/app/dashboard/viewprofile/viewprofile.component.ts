import { Component, OnInit } from '@angular/core';
import { ProfileService } from 'src/app/profile.service';

@Component({
  selector: 'app-viewprofile',
  templateUrl: './viewprofile.component.html',
  styleUrls: ['./viewprofile.component.css']
})
export class ViewProfileComponent implements OnInit {
  profile: any = null;
  profileImageUrl: string | null = null;

  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      this.profileService.getUserProfile(token).subscribe(
        (data: any) => {
          this.profile = data;
          console.log('User profile loaded:', this.profile);
          this.loadProfilePicture();  
        },
        (error: any) => {
          console.error('Error fetching profile:', error);
        }
      );
    } else {
      console.error('No JWT token found');
    }
  }

  loadProfilePicture(): void {
    const username = this.profile?.username;  
    if (username) {
      this.profileService.getProfilePicture(username).subscribe(
        (imageBlob) => {
          const reader = new FileReader();
          reader.onloadend = () => {
            this.profileImageUrl = reader.result as string;
          };
          reader.readAsDataURL(imageBlob);  
        },
        (error) => {
          console.error('Error loading profile picture:', error);
          this.profileImageUrl = 'assets/default-profile-icon.png'; 
        }
      );
    }
  }
}
