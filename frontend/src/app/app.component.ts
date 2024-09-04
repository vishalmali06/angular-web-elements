import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { fromEvent } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  footerurl = 'http://localhost:8000/footer-webcomponent/footer-element.js';
  headerurl = 'http://localhost:8000/header-webcomponent/header-element.js';

  constructor(public router: Router) {}

  ngOnInit(): void {
    fromEvent(window, 'event').subscribe((event: any) => {
      console.log(event);
      this.router.navigate(['user/' + event.detail]);
    });
  }
}
