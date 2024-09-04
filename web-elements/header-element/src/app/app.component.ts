import { Component } from '@angular/core';

@Component({
  selector: 'header-element',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  sendData(data: any){
    const event = new CustomEvent('event', { detail : data });
    dispatchEvent(event);
  }
}
