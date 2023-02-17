/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {EventContainer, EventsContainer} from '../../model/Containers';

@Injectable({
  providedIn: 'root'
})
export class EventsService {
  private endpoint = environment.backendApiUrl + '/events';

  constructor(private http: HttpClient) {
  }

  readAll(): Observable<EventsContainer> {
    return this.http.get<EventsContainer>(this.endpoint + '/getLatest');
  }

  readById(id: string): Observable<EventContainer> {
    return this.http.get<EventContainer>(this.endpoint + `/get/${id}`);
  }

}
