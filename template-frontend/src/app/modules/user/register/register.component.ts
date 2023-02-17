import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../services/auth/auth.service';
import {RegisterForm} from '../../../model/AuthContainer';
import {DateAdapter} from '@angular/material/core';
import {Router} from '@angular/router';
import {ConsoleService} from '../../../services/console/console.service';
import {NotificationService} from '../../../services/notification/notification.service';
import {NotificationType} from '../../../model/Containers';
import {DatePipe} from '@angular/common';
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: any = {};
  signupInfo: RegisterForm;
  isSignUpFailed = false;
  errorMessage = '';
  loading: boolean;

  constructor(private authService: AuthService, private router: Router, private notification: NotificationService,
              private _adapter: DateAdapter<any>, private console: ConsoleService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this._adapter.setLocale(environment.locale);
  }

  onSubmitRegister() {
    this.signupInfo = new RegisterForm(
      this.form.name,
      this.form.username,
      this.form.email,
      this.form.password,
      this.form.dateOfBirth ? this.datePipe.transform(this.form.dateOfBirth, 'dd-MM-yyyy') : null,
      this.form.nickname);
    this.loading = true;
    this.authService.signUp(this.signupInfo).subscribe(
      data => {
        this.notification.showMediumNotification('Successfully registered user!', NotificationType.SUCCESS);
        this.isSignUpFailed = false;
        this.loading = false;
        this.router.navigate(['/login']);
      },
      error => {
        this.notification.showMediumNotification('Error occurred while registering user!', NotificationType.ERROR);
        this.loading = false;
        this.console.log(error);
        this.console.log('error.errors = ', error.error.errors);
        for (let ix in error.error.errors) {
          this.errorMessage += error.error.errors[ix];
        }
        this.isSignUpFailed = true;
      }
    );
  }
}
