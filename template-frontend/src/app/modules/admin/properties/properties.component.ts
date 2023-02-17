import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {ServiceContainer} from '../../../services/service-container';
import {DialogComponent, DialogData} from '../../dialog/dialog.component';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {KeyValue} from '@angular/common';
import {ConfirmationDialog} from '../../dialog/confirmation/confirmation.dialog';
import {NotificationType} from '../../../model/Containers';
import {BehaviorSubject} from 'rxjs';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {debounce} from '../../../decorators';
import {Sort} from '@angular/material/sort';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {
  LARGE_COLUMNS = ['id', 'key', 'value'];
  SHORT_COLUMNS = ['id', 'key'];
  displayedColumns: string[] = this.LARGE_COLUMNS;
  dataSource = new MatTableDataSource<KeyValue<string, any>>([]);
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  data: BehaviorSubject<KeyValue<string, any>[]> = new BehaviorSubject<KeyValue<string, any>[]>([]);

  backendProperties!: KeyValue<string, any>[];
  frontendProperties!: KeyValue<string, any>[];
  loading!: boolean;
  dialogRef!: MatDialogRef<ConfirmationDialog>;

  constructor(private services: ServiceContainer, private dialog: MatDialog) {
    services.console.clazz = 'PropertiesComponent';
  }

  ngOnInit(): void {
    this.getAll();
    this.resize();
  }

  @HostListener('window:resize', ['$event'])
  @debounce(50)
  resize(): void {
    if (window.innerWidth < 992) {
      this.displayedColumns = this.SHORT_COLUMNS;
    } else {
      this.displayedColumns = this.LARGE_COLUMNS;
    }
  }

  @debounce(200)
  filterSearch(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
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
        case 'value':
          return compare(a.value, b.value, isAsc);
        case 'key':
          return compare(a.key, b.key, isAsc);
        default:
          return 0;
      }
    });
  }

  getAll() {
    this.loading = true;
    this.services.properties.getAll().subscribe((response: { properties: KeyValue<string, any>[]; }) => {
      this.backendProperties = response.properties;
      this.loading = false;
      this.appendFrontendProperties();
    }, error => {
      this.backendProperties = [];
      this.loading = false;
      this.appendFrontendProperties();
    });
  }

  appendFrontendProperties() {
    this.dataSource.data = this.backendProperties;
    this.services.util.getAllFromLocalStorage().subscribe(data => {
      this.frontendProperties = data;
      this.services.console.log(this.frontendProperties);
      this.dataSource.data = this.dataSource.data.concat(this.frontendProperties);
    });
    setTimeout(() => this.dataSource.paginator = this.paginator);
  }

  refreshContext() {
    this.loading = true;
    this.services.properties.refreshContext().subscribe((result: { refresh: string | any[]; }) => {
      if (result.refresh.length) {
        this.services.notification.showMediumNotification(`Application context refreshed. Changed keys: ${result.refresh}`, NotificationType.SUCCESS);
      } else {
        this.services.notification.showStickyNotification(`No need to refresh application context`, NotificationType.DEFAULT);
      }
      this.loading = false;
    });
  }

  restartTomcat() {
    this.loading = true;
    this.dialogRef = this.dialog.open(ConfirmationDialog, {
      disableClose: false
    });
    this.dialogRef.componentInstance.confirmMessage = 'Are you sure you want to restart Tomcat server?';

    this.dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.services.properties.restartTomcat().subscribe((result: { restart: any; }) => {
          this.services.console.info(result.restart);
          if (this.loading) {
            this.loading = false;
          }
        });
      }
      this.dialogRef = null;
      this.loading = false;
    });
  }

  loadProperty(property: KeyValue<string, any>) {
    let div = document.createElement('div');

    div.innerHTML = `
        <div class="row">
            <div class="col-12">
                <input class="w-100" disabled value="${property.key}"/>
            </div>
            <div class="col-12">
                <textarea class="w-100 form-control prop-value" rows="10" #propValue>${property.value}</textarea>
            </div>
        </div>`;

    let dialogData = new DialogData();
    dialogData.title = `Edit '${property.key}'`;
    dialogData.titleClasses = 'text-wrap text-truncate';
    dialogData.bodyContainerData = div.innerHTML;
    dialogData.saveAction = () => {
      const textArea: any = $('.prop-value')[0];
      property.value = textArea.value;
      this.saveProperty(property);
    };
    dialogData.saveText = 'Save';
    dialogData.saveBtnClasses = 'mat-stroked-button mat-accent';
    let dialogRef = this.dialog.open(DialogComponent, {data: dialogData});
    dialogRef.afterClosed().subscribe(() => {
    });
  }

  saveProperty(property) {
    this.loading = true;
    this.services.properties.change(property.key, property.value).subscribe(result => {
      this.services.console.info(result.newProperties[0]);
      this.loading = false;
    });
  }
}

// todo move properties to database
// flexible docker config for IP cameras [think of a new way of exposing the camera names]

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
