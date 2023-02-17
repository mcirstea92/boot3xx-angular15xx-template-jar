import {Component, OnInit} from '@angular/core';
import {LoggingService} from '../../../services/utils/logging.service';
import {NotificationService} from '../../../services/notification/notification.service';
import {NotificationType} from '../../../model/Containers';

@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.css']
})
export class LoggingComponent implements OnInit {

  rootLoggerLevel: string;
  loggerPackageLevel: string;
  loggerPackageName: string;
  lastRowNo: string;

  constructor(private loggingService: LoggingService, private notificationService: NotificationService) {
  }

  ngOnInit(): void {
  }

  applyPackageLogLevel() {
    this.loggingService.changeLogLevelForPackage(this.loggerPackageName, this.loggerPackageLevel)
      .subscribe(data =>
        this.notificationService.showNotification(`Log level for package ${this.loggerPackageName} has been changed to ${data.logLevel}`, 3000, NotificationType.SUCCESS)
      );
  }

  applyRootLogLevel() {
    if (this.rootLoggerLevel) {
      this.loggingService.changeRootLogLevel(this.rootLoggerLevel)
        .subscribe(data =>
          this.notificationService.showNotification(`Root log level has been changed to ${data.rootLogLevel}`, 3000, NotificationType.SUCCESS)
        );
    } else {
      this.notificationService.showNotification('Root level is not selected, please choose an option before applying!', 0, NotificationType.ERROR);
    }
  }

  readLastXRowsFromLogFile() {
    this.loggingService.getLastXRowsFromLogFile(Number(this.lastRowNo))
      .subscribe(data =>
        this.notificationService.showNotification(`Lines retrieved from log file:\n ${data.log}`, 0, NotificationType.SUCCESS)
      );
  }
}
