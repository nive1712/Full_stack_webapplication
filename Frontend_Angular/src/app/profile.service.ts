import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = 'http://localhost:8902/auth';

  constructor(private http: HttpClient) {}

  getUserProfile(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get(`${this.apiUrl}/user`, { headers });
  }

 
  uploadProfilePicture(username: string, picture: FormData): Observable<any> {
    const url = `${this.apiUrl}/uploadPicture/${username}`;
    return this.http.post(url, picture, {
      responseType: 'text',
      headers: new HttpHeaders(),
      observe: 'events',
      reportProgress: true
    });
  }

  getProfilePicture(username: string): Observable<Blob> {
    const url = `${this.apiUrl}/getPicture/${username}`;
    return this.http.get(url, { responseType: 'blob' });
  }
}
