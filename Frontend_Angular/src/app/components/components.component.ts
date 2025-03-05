import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-components',
  templateUrl: './components.component.html',
  styleUrls: ['./components.component.css']
})
export class ComponentsComponent implements OnInit, OnDestroy {
  slides = [
    { src: '/assets/slide1.jpg', alt: 'Slide 1' },
    { src: '/assets/slide2.jpg', alt: 'Slide 2' },
    { src: '/assets/slide3.png', alt: 'Slide 3' },
    { src: '/assets/slide4.jpg', alt: 'Slide 4' },
    { src: '/assets/slide5.png', alt: 'Slide 5' },
  ];

  currentSlide: number = 0;
  slideInterval: any;

  ngOnInit() {
    this.startSlideshow();
  }

  ngOnDestroy() {
    if (this.slideInterval) {
      clearInterval(this.slideInterval);
    }
  }

  startSlideshow() {
    this.slideInterval = setInterval(() => {
      this.currentSlide = (this.currentSlide + 1) % this.slides.length;
    }, 4000); 
  }

  scrollTo(section: string) {
    const element = document.getElementById(section);
    if (element) {
      window.scrollTo({ top: element.offsetTop, behavior: 'smooth' });
    }
  }
}
