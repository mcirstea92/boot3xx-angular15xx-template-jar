import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarConfig, MatSnackBarRef} from '@angular/material/snack-bar';
import {NotificationType} from '../../model/Containers';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  currentRefs: MatSnackBarRef<any>[];
  currentRef!: MatSnackBarRef<any>;

  constructor(private _snackBar: MatSnackBar) {
    this.currentRefs = [];
  }

  showNotification(message: string, duration?: number, type?: NotificationType) {
    let config: MatSnackBarConfig = {
      horizontalPosition: 'end',   //this.horizontalPosition [start, left, center, right, end]
      verticalPosition: 'top',      //this.verticalPosition [top, bottom]

      panelClass: 'selected-button-on'
    };
    if (duration && duration > 0) {
      config.duration = duration;
    }
    if (type) {
      config.panelClass = [type, 'notification'];
    } else {
      config.panelClass = [NotificationType.DEFAULT, 'notification'];
    }
    this.currentRef = this._snackBar.open(message, 'X', config);
    this.currentRefs.push(this.currentRef);
  }

  closeNotification() {
    if (this.currentRef) {
      this.currentRef.dismiss();
    }
  }

  showShortNotification(message: string, type?: NotificationType) {
    this.showNotification(message, 2000, type);
  }

  showMediumNotification(message: string, type?: NotificationType) {
    this.showNotification(message, 5000, type);
  }

  showStickyNotification(message: string, type?: NotificationType) {
    this.showNotification(message, -1, type);
  }

  showLongNotification(message: string, type?: NotificationType) {
    this.showNotification(message, 10000, type);
  }

}
