/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {throwError} from 'rxjs';
import {ErrorHandler, Injectable} from '@angular/core';
import {ConsoleService} from '../console/console.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  constructor(private console: ConsoleService) {
    console.clazz = 'ErrorHandlerService';
  }

  handleError(error: any): void {
    let msg = '';
    if (error.error instanceof ErrorEvent) {
      // client side error
      msg = error.error.message;
    } else {
      // server side error
      msg = `Error Code: ${error.status}\nMessage: ${error.message}`;
      if (error.status == 401) {
        this.console.error('401 error detected. ' + msg);
        return;
      }
    }
    throwError(() => msg);
  }

}
