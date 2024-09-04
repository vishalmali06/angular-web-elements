import { Component } from '@angular/core';

@Component({
  selector: 'footer-element',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  currentDate = new Date();
  currentYear = this.currentDate.getFullYear();
}
