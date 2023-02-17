/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {Event, InitHeader} from '../../../model/Containers';
import {Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {ServiceContainer} from '../../../services/service-container';
import {debounce} from '../../../decorators';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit, InitHeader {

  events: Event[];
  event: Event;
  showAll = false;
  displayedColumns: string[];
  dataSource = new MatTableDataSource<Event>([]);
  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  loading: boolean;
  headerTextValue: string;
  apiKey: string = '32c8677db40d27c4e2e74705ad658d43';
  largerWidth: boolean;

  private LARGE_COLUMNS = ['id', 'description', 'type', 'created', 'additionalInfo', 'user', 'ip'];
  private SHORT_COLUMNS = ['id', 'description', 'created', 'additionalInfo'];

  constructor(private services: ServiceContainer) {
  }

  ngOnInit(): void {
    this.largerWidth = false;
    this.loadData();
    this.initHeader('Events stored in database over time');
    this.onResize();
  }

  @HostListener('window:resize', ['$event'])
  @debounce(50)
  onResize(): void {
    if (window.innerWidth < 992) {
      this.displayedColumns = this.SHORT_COLUMNS;
      const pagSize = window.setInterval(() => {
        if (this.paginator) {
          this.paginator.pageSize = 8;
          this.dataSource.paginator = this.paginator;
          window.clearInterval(pagSize);
        }
      }, 1000);
    } else {
      this.displayedColumns = this.LARGE_COLUMNS;
    }
  }

  @debounce(200)
  filterSearch(event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getEventById(id: string) {
    this.services.events.readById(id).subscribe(data => this.event = data.event);
  }

  editEvent(item: Event) {
    alert('Need to implement edit event');
  }

  loadData() {
    this.loading = true;
    this.services.events.readAll().subscribe({
      next: data => {
        this.dataSource.data = data.events;
        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
        });
        this.loading = false;
        this.initHeader('Events stored in database over time - ' + this.dataSource.data.length + ' retrieved right now');
      }
    });
  }

  sortData(sort: Sort) {
    const data = this.dataSource.data.slice();
    if (!sort.active || sort.direction === '') {
      this.dataSource.data = data;
      return;
    }

    this.dataSource.data = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'created':
          return compare(a.createdTimestamp, b.createdTimestamp, isAsc);
        case 'type':
          return compare(a.type, b.type, isAsc);
        case 'id':
          return compare(a.id, b.id, isAsc);
        default:
          return 0;
      }
    });
  }

  initHeader(text: string): void {
    this.headerTextValue = text;
  }

  retrieveIpDetails(ip: string) {
    const fullApiIp = `http://api.ipstack.com/ip?${this.apiKey}`;
    return 'Romania';
  }

}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
