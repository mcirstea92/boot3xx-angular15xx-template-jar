/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {LastLinesLogContainer, LogLevelContainer, RootLogLevelContainer} from '../../model/Containers';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoggingService {

  private endpoint = environment.backendApiUrl + '/logging';

  constructor(private http: HttpClient) {
  }

  changeLogLevelForPackage(packageName: string, newLogLevel: string): Observable<LogLevelContainer> {
    return this.http.get<LogLevelContainer>(`${this.endpoint}/setLevel/${packageName}/${newLogLevel}`);
  }

  changeRootLogLevel(newRootLogLevel: string): Observable<RootLogLevelContainer> {
    return this.http.get<RootLogLevelContainer>(`${this.endpoint}/setRootLevel/${newRootLogLevel}`);
  }

  getLastXRowsFromLogFile(rowNo: number): Observable<LastLinesLogContainer> {
    return this.http.get<LastLinesLogContainer>(`${this.endpoint}/retrieveLastLines/${rowNo}`);
  }

}
