import {Component, OnInit} from '@angular/core';
import {LoginForm} from '../../../model/AuthContainer';
import {AuthService} from '../../../services/auth/auth.service';
import {TokenStorageService} from '../../../services/auth/token-storage.service';
import {Router} from '@angular/router';
import {ConsoleService} from '../../../services/console/console.service';
import {NotificationType} from '../../../model/Containers';
import {NotificationService} from '../../../services/notification/notification.service';
import {translate} from "@ngneat/transloco";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  private loginInfo: LoginForm;
  loading: boolean;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService, private router: Router,
              private notification: NotificationService, private console: ConsoleService) {
    console.clazz = 'LoginComponent';
  }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getAuthorities();
    }
  }

  onSubmitLogin() {
    if (this.form.password.length < 8) {
      this.errorMessage = 'Password should be at least 8 characters long';
      this.isLoginFailed = true;
      return;
    }
    this.loginInfo = new LoginForm(this.form.username, this.form.password);
    this.loading = true;
    this.authService.attemptAuth(this.loginInfo).subscribe({
      next:
        data => {
          this.notification.showMediumNotification(translate('successfully_logged_in'), NotificationType.SUCCESS);
          this.loading = false;
          this.tokenStorage.saveToken(data.accessToken);
          this.tokenStorage.saveUsername(data.username);
          this.tokenStorage.saveName(data.name);
          this.tokenStorage.saveEmail(data.email);
          this.tokenStorage.saveUserId(data.userId);
          this.tokenStorage.saveAuthorities(data.authorities);
          this.tokenStorage.saveRefreshToken(data.refreshToken);
          this.tokenStorage.userLoggedIn();

          this.isLoginFailed = false;
          this.isLoggedIn = true;
          this.roles = this.tokenStorage.getAuthorities();

          this.router.navigate(['/acasa']);
          //window.location.reload();
        }, error: error => {
        this.notification.showMediumNotification(translate('error_signing_in'), NotificationType.ERROR);

        this.loading = false;
        this.console.error('Error while logging in: ', error);
        this.errorMessage = error?.error?.error || error?.error || error;
        this.isLoginFailed = true;
      }
    });
  }

}
