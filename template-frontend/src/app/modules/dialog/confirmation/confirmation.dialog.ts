import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'confirm-dialog',
  templateUrl: 'confirmation.dialog.html',
})
export class ConfirmationDialog {
  constructor(public dialogRef: MatDialogRef<ConfirmationDialog>) {
  }

  public confirmMessage: string;
}
