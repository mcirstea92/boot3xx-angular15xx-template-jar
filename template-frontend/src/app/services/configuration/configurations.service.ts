/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {Config, ConfigContainer, ConfigsContainer} from '../../model/Containers';
import {UtilService} from "../utils/util.service";
import {TranslocoService} from "@ngneat/transloco";
import {ConsoleService} from "../console/console.service";
import {Constants} from "../utils/constants";

@Injectable({
  providedIn: 'root'
})
export class ConfigurationsService {
  private endpoint = environment.backendApiUrl + '/configs';
  private publicEndpoint = environment.backendApiUrl + '/configs/public';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient, private utils: UtilService, private transloco: TranslocoService, private console: ConsoleService) {
    console.clazz = 'ConfigurationsService';
  }

  readLatest(): Observable<ConfigsContainer> {
    return this.http.get<ConfigsContainer>(this.publicEndpoint + '/getLatest');
  }

  readAll(): Observable<ConfigsContainer> {
    return this.http.get<ConfigsContainer>(this.publicEndpoint + '/getAll');
  }

  saveConfig(config: Config): Observable<ConfigContainer> {
    return this.http.post<ConfigContainer>(this.endpoint + '/saveConfiguration', config, this.httpOptions);
  }

  syncDBConfigsToLocalStorage() {
    this.readLatest().subscribe({
      next: data => {
        this.utils.setLocalStorage(Constants.CONFIG_KEY, data.configs);
      }
    })
  }

  getConfigsFromLocalStorage() {
    this.syncDBConfigsToLocalStorage();
    return this.utils.syncGet(Constants.CONFIG_KEY);
  }

  getBrowserConfigs(): Observable<any> {
    return this.utils.getFromLocalStorage(Constants.BROWSER_KEY);
  }

  synchronisePreferences() {
    const defaultLang = environment.language;
    this.getBrowserConfigs().subscribe({
      next: values => {
        if (values == undefined) {
          let values = {};
          values[Constants.LANGUAGE_KEY] = navigator.language || window.navigator.language;
          this.utils.setLocalStorage(Constants.BROWSER_KEY, values);
          this.synchronisePreferences();
        } else {
          const availableLangs = this.transloco.getAvailableLangs().map(l => l || l.label);
          const browserLang = values[Constants.LANGUAGE_KEY];
          let langFound = availableLangs.filter(lang => browserLang.indexOf(lang) !== -1).length === 1;
          if (langFound) {
            let lang = availableLangs.find(lang => browserLang.indexOf(lang) !== -1);
            this.changeLanguageTo(lang);
          } else {
            this.changeLanguageTo(defaultLang);
          }
        }
      }
    })
  }

  switchLanguage() {
    const crtLang: string = this.transloco.getActiveLang();
    const availableLangs = this.transloco.getAvailableLangs().map(l => l || l.label);
    const crtIndex = availableLangs.indexOf(crtLang);
    const nextIndex = (crtIndex + 1) % availableLangs.length;
    let nextLang = availableLangs[nextIndex];
    this.changeLanguageTo(nextLang);
  }

  changeLanguageTo(newLang: string) {
    const availableLangs = this.transloco.getAvailableLangs().map(l => l || l.label);
    let langFound = availableLangs.filter(lang => lang === newLang).length === 1;
    if (langFound) {
      this.transloco.setActiveLang(newLang);
    } else {
      this.console.error('Language ' + newLang + ' not available!');
    }
  }

}
