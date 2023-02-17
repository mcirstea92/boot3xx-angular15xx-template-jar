import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {JwtResponse, LoginForm, RegisterForm} from '../../model/AuthContainer';
import {environment} from '../../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private endpoint = environment.backendApiUrl + '/auth';

  private loginUrl = `${this.endpoint}/login`;
  private signupUrl = `${this.endpoint}/signup`;
  private refreshTokenUrl = `${this.endpoint}/refreshToken`;
  private logoutUrl = `${this.endpoint}/logout`;

  constructor(private http: HttpClient) {
  }

  attemptAuth(credentials: LoginForm): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(this.loginUrl, credentials);
  }

  signUp(info: RegisterForm): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(this.signupUrl, info);
  }

  refreshToken(token: string) {
    return this.http.post<JwtResponse>(this.refreshTokenUrl, {refreshToken: token});
  }

  logout() {
    return this.http.post<string>(this.logoutUrl, {});
  }

}
