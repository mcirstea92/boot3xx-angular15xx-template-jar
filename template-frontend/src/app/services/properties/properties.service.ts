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
import {
  ChangedProperties,
  ExecutionCommandResult,
  Properties,
  Property,
  RefreshContextResult,
  RestartTomcatResult
} from '../../model/Containers';

@Injectable({
  providedIn: 'root'
})
export class PropertiesService {
  private endpoint = environment.backendApiUrl + '/properties';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Properties> {
    return this.http.get<Properties>(this.endpoint + '/all');
  }

  getProperty(key: string): Observable<Property> {
    return this.http.get<Property>(this.endpoint + `/get/${key}`);
  }

  refreshContext(): Observable<RefreshContextResult> {
    return this.http.get<RefreshContextResult>(this.endpoint + '/refreshContext');
  }

  restartTomcat(): Observable<RestartTomcatResult> {
    return this.http.get<RestartTomcatResult>(this.endpoint + '/restartTomcat');
  }

  change(key: string, value: any): Observable<ChangedProperties> {
    let body = [];
    body.push({key: key, value: value});
    return this.http.post<ChangedProperties>(this.endpoint + '/change', body);
  }

  executeCommand(jsonExecutionCommand: any) {
    return this.http.post<ExecutionCommandResult>(this.endpoint + '/executeCommand', jsonExecutionCommand);
  }

}
