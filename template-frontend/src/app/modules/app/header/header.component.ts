import {Component, Input} from '@angular/core';

@Component({
  selector: 'custom-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  @Input('text')
  text?: string;

  @Input('shiftLeft')
  shiftLeft?: boolean = false;

}
