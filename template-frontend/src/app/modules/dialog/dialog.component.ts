/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';

@Component({
  selector: 'dialog-component',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent {

  dialogBodyContainer: SafeHtml;

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData<any, any>, private dialogRef: MatDialogRef<DialogData<any, any>>, private sanitizer: DomSanitizer) {
    this.sanitizer.bypassSecurityTrustHtml(data.title);
    if (data && data.bodyContainerData) {
      this.dialogBodyContainer = this.sanitizer.bypassSecurityTrustHtml(data.bodyContainerData);
    }
  }

  close(): void {
    this.dialogRef.close(true);
  }

  saveAction(): any {
    if (this.data && this.data.saveAction) {
      this.data.saveAction();
      this.dialogRef.close(true);
    }
  }

  saveNoDismiss(): any {
    if (this.data && this.data.saveNoDismiss) {
      this.data.saveNoDismiss();
    }
  }

}

export class DialogData<T, P> {
  data: T;
  parentComponent?: any;
  title: string;
  body: string;
  subtitle?: string;
  closeText?: string = 'Close';
  closeBtnClasses?: string = 'mat-stroked-button mat-warn';
  titleClasses?: string = '';
  bodyContainerData?: string; // html body
  saveBtnClasses?: string = '';
  saveAction?: () => any;
  saveNoDismiss?: () => any;
  saveText?: string = undefined;

  public static authFailed(): DialogData<any, undefined> {
    let dialogData = new DialogData();
    dialogData.title = 'Autentificare eșuată';
    dialogData.body = 'Valabilitatea tokenului de autentificare a expirat, vă rugăm să vă autentificați din nou.';
    dialogData.subtitle = 'Sesiunea a expirat';
    dialogData.closeText = 'Mergi către pagina de login';
    dialogData.closeBtnClasses = 'mat-warn';
    dialogData.titleClasses = 'alert alert-danger';
    return dialogData;
  }

  public static authWillFail(refreshToken?): DialogData<any, undefined> {
    let dialogData = new DialogData();
    dialogData.title = 'Delogare iminentă';
    dialogData.body = 'Valabilitatea tokenului de autentificare va expira în mai puțin de 5 minute, salvați-vă munca pentru a nu pierde datele.';
    dialogData.subtitle = 'Sesiunea va expira curând';
    dialogData.closeText = 'Ok';
    dialogData.saveText = 'Prelungire valabilitate';
    dialogData.saveAction = () => {
      if (refreshToken) {
        refreshToken();
      }
    };
    dialogData.closeBtnClasses = 'mat-info';
    dialogData.titleClasses = 'info';
    return dialogData;
  }

}
