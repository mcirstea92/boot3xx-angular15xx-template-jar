import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'hr-separator',
  templateUrl: './hr-separator.component.html',
  styleUrls: ['./hr-separator.component.css']
})
export class HrSeparatorComponent implements OnInit {

  @Input()
  label: string;

  constructor() {
  }

  ngOnInit(): void {
  }

}
