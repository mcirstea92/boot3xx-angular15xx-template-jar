/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {DatePipe} from '@angular/common';
import {Pipe, PipeTransform} from '@angular/core';
import {UtilService} from "../services/utils/util.service";

@Pipe({
  name: 'localizedDate',
  pure: false
})
export class LocalizedDatePipe implements PipeTransform {

  constructor(private utilsService: UtilService) {
  }

  transform(value: any, pattern: string = 'mediumDate', locale?: string): any {
    if (!locale) {
      locale = this.utilsService.getActiveLocale();
    }
    const datePipe: DatePipe = new DatePipe(locale);
    return datePipe.transform(value, pattern);
  }

}
