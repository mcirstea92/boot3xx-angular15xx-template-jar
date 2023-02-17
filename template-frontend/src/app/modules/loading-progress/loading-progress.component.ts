import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-loading-progress',
  templateUrl: './loading-progress.component.html',
  styleUrls: ['./loading-progress.component.css']
})
export class LoadingProgressComponent implements OnInit {

  @Input('loading')
  loading: boolean;

  @Input('loadingText')
  loadingText?: string;
  color: 'primary' | 'accent' | 'warn' = 'primary';

  constructor() {
  }

  ngOnInit(): void {
  }

}
