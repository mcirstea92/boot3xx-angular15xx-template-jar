/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Injectable} from '@angular/core';
import {AuthService} from './auth/auth.service';
import {UserService} from './user/user.service';
import {ErrorHandlerService} from './utils/error.handler.service';
import {LoggingService} from './utils/logging.service';
import {UtilService} from './utils/util.service';
import {HttpClient} from '@angular/common/http';
import {NotificationService} from "./notification/notification.service";
import {EventsService} from "./events/events.service";
import {PropertiesService} from "./properties/properties.service";
import {ConsoleService} from "./console/console.service";
import {TranslocoService} from "@ngneat/transloco";
import {ConfigurationsService} from "./configuration/configurations.service";

@Injectable({
  providedIn: 'root'
})
export class ServiceContainer {

  constructor(private _auth: AuthService, private _console: ConsoleService, private _events: EventsService,
              private _notification: NotificationService, private _user: UserService, private _errorHandler: ErrorHandlerService,
              private _logging: LoggingService, private _util: UtilService, private _http: HttpClient, private _properties: PropertiesService,
              private _transloco: TranslocoService, private _config: ConfigurationsService) {
  }

  get auth(): AuthService {
    return this._auth;
  }

  get console(): ConsoleService {
    return this._console;
  }

  get events(): EventsService {
    return this._events;
  }

  get notification(): NotificationService {
    return this._notification;
  }

  get user(): UserService {
    return this._user;
  }

  get error(): ErrorHandlerService {
    return this._errorHandler;
  }

  get logging(): LoggingService {
    return this._logging;
  }

  get util(): UtilService {
    return this._util;
  }

  get http(): HttpClient {
    return this._http;
  }

  get properties(): PropertiesService {
    return this._properties;
  }

  get transloco(): TranslocoService {
    return this._transloco;
  }

  get config(): ConfigurationsService {
    return this._config;
  }

}
