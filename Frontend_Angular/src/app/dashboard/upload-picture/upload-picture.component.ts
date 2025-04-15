import { Component } from '@angular/core';
import { ProfileService } from 'src/app/profile.service';

@Component({
  selector: 'app-upload-picture',
  templateUrl: './upload-picture.component.html',
  styleUrls: ['./upload-picture.component.css']
})
export class UploadPictureComponent {
  username: string = '';  
  selectedFile: File | null = null;
  profileImageUrl: string | null = null;

  constructor(private profileService: ProfileService) {}

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadPicture(): void {
    if (this.username && this.selectedFile) {
      const formData = new FormData();
      formData.append('picture', this.selectedFile, this.selectedFile.name);

      this.profileService.uploadProfilePicture(this.username, formData)
        .subscribe(
          () => {
            console.log('Picture uploaded successfully');
            this.loadProfilePicture(); 
          },
          (error: any) => {
            console.error('Failed to upload picture', error);
          }
        );
    } else {
      console.error('Please select a file and enter a username.');
    }
  }

  loadProfilePicture(): void {
    if (this.username) {
      this.profileService.getProfilePicture(this.username)
        .subscribe((imageBlob) => {
          const reader = new FileReader();
          reader.onloadend = () => {
            this.profileImageUrl = reader.result as string;
          };
          reader.readAsDataURL(imageBlob);
        });
    }
  }
}
