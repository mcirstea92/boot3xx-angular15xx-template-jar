import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';

import {TokenStorageService} from './token-storage.service';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {ConsoleService} from "../console/console.service";
import {Constants} from "../utils/constants";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private token: TokenStorageService, private authService: AuthService, private console: ConsoleService) {
    this.console.clazz = 'AuthInterceptor';
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const token = this.token.getToken();
    if (req.headers.get(Constants.CONTENT_TYPE_KEY) == undefined) {
      req = req.clone({headers: req.headers.set(Constants.CONTENT_TYPE_KEY, Constants.APPLICATION_JSON_TYPE)});
    }
    if (token != null) {
      authReq = req.clone({headers: req.headers.set(Constants.TOKEN_HEADER_KEY, 'Bearer ' + token)});
    }
    return next.handle(authReq);
  }

}

export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
];
