import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {TokenStorageService} from './token-storage.service';
import {DialogData} from '../../modules/dialog/dialog.component';
import {lastValueFrom} from "rxjs";
import {AuthService} from "./auth.service";
import {Constants} from "../utils/constants";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private token: TokenStorageService, private authService: AuthService) {
  }

  async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const token = this.token.getToken();
    if (token && !this.token.isExpired()) {
      return true;
    }

    const isRefreshSuccess = await this.refreshToken();
    if (!isRefreshSuccess) {
      this.token.signOut(DialogData.authFailed());
    }
    return isRefreshSuccess;
  }

  private async refreshToken(): Promise<boolean> {
    const refreshToken: string | null = window.sessionStorage.getItem(Constants.REFRESH_TOKEN_KEY);

    if (!refreshToken) {
      return false;
    }

    let isRefreshSuccess: boolean;
    try {
      const response = await lastValueFrom(this.authService.refreshToken(refreshToken));
      window.sessionStorage.setItem(Constants.TOKEN_KEY, response.accessToken);
      window.sessionStorage.setItem(Constants.REFRESH_TOKEN_KEY, response.refreshToken);
      isRefreshSuccess = true;
    } catch (ex) {
      isRefreshSuccess = false;
    }
    return isRefreshSuccess;
  }

}
