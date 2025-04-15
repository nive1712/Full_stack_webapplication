import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-netbanking',
  templateUrl: './netbanking.component.html',
  styleUrls: ['./netbanking.component.css']
})
export class NetbankingComponent {

  constructor(private router: Router) {}
  navigateTo(path: string) {
    this.router.navigate([path]);
  }

}
