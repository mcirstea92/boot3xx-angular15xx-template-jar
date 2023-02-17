import {CUSTOM_ELEMENTS_SCHEMA, ErrorHandler, LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppRoutingModule} from './app.routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AngularMaterialModule} from './angular-material.module';
import ro from '@angular/common/locales/ro';
import {NgSelectModule} from "@ng-select/ng-select";
import {MatSelectModule} from "@angular/material/select";
import {MatTreeModule} from "@angular/material/tree";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {PropertiesComponent} from "./modules/admin/properties/properties.component";
import {EventsComponent} from "./modules/admin/events/events.component";
import {HomePageComponent} from "./modules/home-page/home-page.component";
import {ConfirmationDialog} from "./modules/dialog/confirmation/confirmation.dialog";
import {DialogComponent} from "./modules/dialog/dialog.component";
import {HeaderComponent} from "./modules/app/header/header.component";
import {FooterComponent} from "./modules/app/footer/footer.component";
import {APP_BASE_HREF, DatePipe, HashLocationStrategy, LocationStrategy, registerLocaleData} from "@angular/common";
import {ErrorHandlerService} from "./services/utils/error.handler.service";
import {AuthGuard} from "./services/auth/auth.guard";
import {httpInterceptorProviders} from "./services/auth/auth.interceptor";
import {CustomKeyValuePipePipe} from "./pipes/custom-key-value-pipe.pipe";
import {LocalizedDatePipe} from "./pipes/localized.date.pipe";
import {ReadMorePipe} from "./pipes/read.more.pipe";
import {LoginComponent} from "./modules/user/login/login.component";
import {RegisterComponent} from "./modules/user/register/register.component";
import {AdminComponent} from "./modules/user/admin/admin.component";
import {UserComponent} from "./modules/user/user/user.component";
import {AppComponent} from "./modules/app/app.component";
import {HrSeparatorComponent} from "./modules/hr-separator/hr-separator.component";
import {LoadingProgressComponent} from "./modules/loading-progress/loading-progress.component";
import {LoggingComponent} from './modules/admin/logging/logging.component';
import {TranslocoRootModule} from './transloco-root.module';

registerLocaleData(ro);

@NgModule({

  declarations: [
    EventsComponent,
    AppComponent,
    ReadMorePipe,
    LocalizedDatePipe,
    CustomKeyValuePipePipe,
    LoggingComponent,
    LoginComponent,
    RegisterComponent,
    UserComponent,
    AdminComponent,
    LoadingProgressComponent,
    HeaderComponent,
    FooterComponent,
    DialogComponent,
    HrSeparatorComponent,
    PropertiesComponent,
    ConfirmationDialog,
    HomePageComponent
  ],
  exports: [MatInputModule],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    NgbModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatTreeModule,
    TranslocoRootModule
  ],
  providers: [
    {provide: APP_BASE_HREF, useValue: '/'},
    {provide: LOCALE_ID, useValue: 'ro-RO'},
    {provide: ErrorHandler, useClass: ErrorHandlerService},
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    httpInterceptorProviders,
    DatePipe,
    AuthGuard],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
