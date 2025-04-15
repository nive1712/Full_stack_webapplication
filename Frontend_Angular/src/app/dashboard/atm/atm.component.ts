import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-atm',
  templateUrl: './atm.component.html',
  styleUrls: ['./atm.component.css']
})
export class AtmComponent {
  
  private currentUrl: string = '';

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentUrl = event.urlAfterRedirects;
        this.scrollToForm(this.currentUrl);
      }
    });
  }

  navigateAndScroll(route: string): void {
    this.router.navigate([route]);
  }

  private scrollToForm(url: string): void {
    let elementClass = '';
    if (url.includes('deposit')) {
      elementClass = 'deposit-container';
    } else if (url.includes('withdraw')) {
      elementClass = 'withdraw-container';
    } else if (url.includes('transfer')) {
      elementClass = 'transfer-container';
    }

    if (elementClass) {
      setTimeout(() => {
        const element = document.querySelector('.' + elementClass);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth' });
        }
      }, 100);
    }
  }
}

