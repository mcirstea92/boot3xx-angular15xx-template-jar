/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {DialogComponent, DialogData} from '../../modules/dialog/dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {BehaviorSubject, Observable} from 'rxjs';
import {AuthService} from "./auth.service";
import {ConsoleService} from "../console/console.service";
import {JwtResponse} from "../../model/AuthContainer";
import {Constants} from "../utils/constants";

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private roles: Array<string> = [];

  loggedIn = new BehaviorSubject<boolean>(true);
  previousLoggedIn = new BehaviorSubject<boolean>(false);
  timeoutWarningShown = false;
  logInInterval: number;

  constructor(private router: Router, private dialog: MatDialog, private authService: AuthService, private console: ConsoleService) {
  }

  loggedInUserIsAdmin() {
    this.isLoggedIn();
    return this.getAuthorities().some(role => role.trim().toLowerCase().indexOf('admin') !== -1);
  }

  userLoggedIn() {
    this.previousLoggedIn.next(true);
    this.loggedIn.next(true);
    if (!this.logInInterval) {
      this.logInInterval = window.setInterval(() => {
        this.isLoggedIn();
      }, 5_000);
    }
  }

  signOut(dialogData?: DialogData<any, any>) {
    window.sessionStorage.clear();
    this.loggedIn.next(false);
    this.dialog.closeAll();
    if (dialogData) {
      let dialogRef = this.dialog.open(DialogComponent, {data: dialogData});
      dialogRef.afterClosed().subscribe(next => {
        this.router.navigate(['/login']);
      });
    } else {
      this.previousLoggedIn.next(false);
      this.loggedIn.next(false);
      this.router.navigate(['/login']);
    }
    clearInterval(this.logInInterval);
    this.logInInterval = null;
    this.timeoutWarningShown = false;
  }

  public saveToken(token: string) {
    window.sessionStorage.removeItem(Constants.TOKEN_KEY);
    window.sessionStorage.setItem(Constants.TOKEN_KEY, token);
  }

  public saveName(name: string) {
    window.sessionStorage.removeItem(Constants.NAME_KEY);
    window.sessionStorage.setItem(Constants.NAME_KEY, name);
  }

  public saveEmail(email: string) {
    window.sessionStorage.removeItem(Constants.EMAIL_KEY);
    window.sessionStorage.setItem(Constants.EMAIL_KEY, email);
  }

  public getToken(): string {
    return sessionStorage.getItem(Constants.TOKEN_KEY);
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(Constants.USERNAME_KEY);
    window.sessionStorage.setItem(Constants.USERNAME_KEY, username);
  }

  saveUserId(userId: number) {
    window.sessionStorage.removeItem(Constants.USERID_KEY);
    window.sessionStorage.setItem(Constants.USERID_KEY, `${userId}`);
  }

  public getUsername(): string {
    return sessionStorage.getItem(Constants.USERNAME_KEY);
  }

  public getName(): string {
    return sessionStorage.getItem(Constants.NAME_KEY);
  }

  public getEmail(): string {
    return sessionStorage.getItem(Constants.EMAIL_KEY);
  }

  public getUserId(): string {
    return sessionStorage.getItem(Constants.USERID_KEY);
  }

  public getIssuedAt(): string {
    const token = this.getToken();
    if (!token) {
      return;
    }
    return TokenStorageService.parseJwt(token).iat;
  }

  public getExpiresAt(): string {
    const token = this.getToken();
    if (!token) {
      return;
    }
    return TokenStorageService.parseJwt(token).exp;
  }

  public saveAuthorities(authorities: string[]) {
    window.sessionStorage.removeItem(Constants.AUTHORITIES_KEY);
    window.sessionStorage.setItem(Constants.AUTHORITIES_KEY, JSON.stringify(authorities));
  }

  public getAuthorities(): string[] {
    this.roles = [];

    if (sessionStorage.getItem(Constants.AUTHORITIES_KEY)) {
      JSON.parse(sessionStorage.getItem(Constants.AUTHORITIES_KEY)).forEach(authority => {
        this.roles.push(authority.authority);
      });
    }
    return this.roles;
  }

  public isExpired() {
    const nowDate = Date.now() / 1000;
    const exp = this.getExpiresAt();
    return Number(exp) < nowDate && !isNaN(Number(exp));
  }

  public isLoggedIn() {
    const nowDate = Date.now() / 1000;
    const exp = this.getExpiresAt();
    const isExpired = this.isExpired();
    const secondsTillExpiry = Math.floor(Number(exp) - nowDate);
    if (!isExpired) {
      if (secondsTillExpiry < 300 && !this.timeoutWarningShown) {
        //todo configurable timeout warning
        let dialogData = DialogData.authWillFail(() => {
          this.executeRefreshToken().subscribe({
              next: data => {
                this.saveRefreshToken(data.refreshToken);
                this.saveToken(data.accessToken);
              }
            }
          );
        });
        this.dialog.open(DialogComponent, {data: dialogData});
        this.timeoutWarningShown = true;
      }
    }
  }

  public executeRefreshToken(): Observable<JwtResponse> {
    //this.console.log('Old AT: ' + this.getToken());
    return this.authService.refreshToken(this.getRefreshToken());
  }

  private static parseJwt(token) {
    if (!token) {
      return;
    }
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  }

  saveRefreshToken(refreshToken: string) {
    window.sessionStorage.removeItem(Constants.REFRESH_TOKEN_KEY);
    window.sessionStorage.setItem(Constants.REFRESH_TOKEN_KEY, refreshToken);
  }

  public getRefreshToken(): string {
    return sessionStorage.getItem(Constants.REFRESH_TOKEN_KEY);
  }

}
