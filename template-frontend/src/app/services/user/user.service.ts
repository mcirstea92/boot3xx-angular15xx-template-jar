/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {tap} from 'rxjs/operators';
import {ConsoleService} from '../console/console.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private endpoint = environment.backendApiUrl + '/user';

  private userUrl = `${this.endpoint}/test/user`;
  private pmUrl = `${this.endpoint}/test/pm`;
  private adminUrl = `${this.endpoint}/test/admin`;

  constructor(private httpClient: HttpClient, private console: ConsoleService) {
    console.clazz = 'UserService';
  }

  getUserBoard(): Observable<string> {
    return this.httpClient.get<string>(this.userUrl)
      .pipe(
        tap(result => this.console.log('Called user board: ', result))
      );
  }

  getPMBoard(): Observable<string> {
    return this.httpClient.get<string>(this.pmUrl)
      .pipe(
        tap(result => this.console.log('Called pm board: ', result))
      );
  }

  getAdminBoard(): Observable<string> {
    return this.httpClient.get<string>(this.adminUrl)
      .pipe(
        tap(result => this.console.log('Called admin board: ', result))
      );
  }

}
