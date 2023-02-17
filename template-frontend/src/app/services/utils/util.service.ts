/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {BehaviorSubject} from 'rxjs';
import {DatePipe, isPlatformBrowser, KeyValue} from '@angular/common';
import {environment} from '../../../environments/environment';
import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {MatMenuTrigger} from '@angular/material/menu';
import {TranslocoService} from "@ngneat/transloco";
import {LOCALES} from "../../model/System";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(private localStorage: LocalStorageService, private datePipe: DatePipe, private translocoService: TranslocoService) {
  }

  getAllFromLocalStorage() {
    return this.localStorage.getAll();
  }

  getFromLocalStorage(key: string) {
    return this.localStorage.get(key);
  }

  setLocalStorage(key: string, value: any) {
    return this.localStorage.set(key, value);
  }

  clearLocalStorage(key?: string) {
    if (key) {
      return this.localStorage.clear(key);
    } else {
      return this.localStorage.clearAllLocalStorage();
    }
  }

  // another util implementations?

  formatDate(date: Date, format: string) {
    return this.datePipe.transform(date, format != null ? format : environment.dateTimeFormat);
  }

  formatDateWithLocale(date: Date, format: string, locale: string) {
    return this.datePipe.transform(date, format != null ? format : environment.dateTimeFormat, locale != null ? locale : environment.locale);
  }

  openContextMenu(event: MouseEvent, contextMenuPosition: { x, y }, contextMenu: MatMenuTrigger, item: any) {
    event.preventDefault();
    contextMenuPosition.x = event.clientX + 'px';
    contextMenuPosition.y = event.clientY + 'px';
    contextMenu.menuData = {'item': item};
    contextMenu.menu.focusFirstItem('mouse');
    contextMenu.openMenu();
  }

  minutesToHHmm = (m) => {
    const hours = Math.floor(m / 60);
    const minutes = m - hours * 60;
    return (
      hours.toString().padStart(2, '0') +
      'h:' +
      minutes.toString().padStart(2, '0') + 'm'
    );
  };

  addIfNotContains<T>(array: T[], element: T, position?: number) {
    if (array.indexOf(element) === -1) {
      if (position !== undefined) {
        array.splice(position, 0, element);
      } else {
        array.push(element);
      }
    }
    return array;
  }

  changeItemIndex<T>(array: T[], element: T, newPositionIndex: number) {
    const fromIndex = array.indexOf(element);
    element = array.splice(fromIndex, 1)[0];
    array.splice(newPositionIndex, 0, element);
  }

  update(masterKey: string, key: string, value: string) {
    const data = this.syncGet(key);
    if (data == undefined) {
      let map = {};
      map[key] = value;
      this.syncSetObject(masterKey, map);
    } else {
      data[key] = value;
      this.syncSetObject(masterKey, data);
    }
  }

  syncGet(key: string): any {
    return JSON.parse(getLocalStorage().getItem(key));
  }

  syncSet(key: string, value: string) {
    return getLocalStorage().setItem(key, value);
  }

  syncSetObject(key: string, value: any) {
    return getLocalStorage().setItem(key, JSON.stringify(value));
  }

  getActiveLanguage() {
    return LOCALES[this.translocoService.getActiveLang()].language;
  }

  getActiveLocale() {
    return LOCALES[this.translocoService.getActiveLang()].locale;
  }

}

function getLocalStorage() {
  return localStorage;
}

@Injectable({providedIn: 'root'})
export class LocalStorageRefService {

  constructor(@Inject(PLATFORM_ID) private platformId) {
  }

  get localStorage() {
    if (isPlatformBrowser(this.platformId)) {
      return getLocalStorage();
    } else {
      // use alternative or throw error
    }
  }
}

@Injectable({providedIn: 'root'})
export class LocalStorageService {
  private _localStorage: Storage;

  constructor(private _localStorageRefService: LocalStorageRefService) {
    this._localStorage = _localStorageRefService.localStorage;
  }

  private _myData$ = new BehaviorSubject<any>(null);

  set(key: string, data: any) {
    const jsonData = JSON.stringify(data);
    this._localStorage.setItem(key, jsonData);
    this._myData$.next(data);
  }

  get(key: string) {
    const data = JSON.parse(this._localStorage.getItem(key));
    this._myData$.next(data);
    return this._myData$.asObservable();
  }

  clear(key: string) {
    this._localStorage.removeItem(key);
    this._myData$.next(null);
  }

  clearAllLocalStorage() {
    this._localStorage.clear();
    this._myData$.next(null);
  }

  getAll() {
    let arr: KeyValue<any, any>[] = [];
    for (let i = 0; i < this._localStorage.length; i++) {
      arr.push({key: this._localStorage.key(i), value: this._localStorage.getItem(this._localStorage.key(i))});
    }
    this._myData$.next(arr);
    return this._myData$.asObservable();
  }

}
