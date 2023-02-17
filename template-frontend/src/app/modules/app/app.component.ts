import {Component, OnDestroy, OnInit} from '@angular/core';
import {TokenStorageService} from '../../services/auth/token-storage.service';

import {ConsoleService} from '../../services/console/console.service';
import {ConfigurationsService} from "../../services/configuration/configurations.service";
import {TranslocoService} from "@ngneat/transloco";
import {UtilService} from "../../services/utils/util.service";
import {Constants} from "../../services/utils/constants";

@Component({
  selector: 'app-dashboard',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  isMenuCollapsed = true;
  time: number;
  timerId: number;

  headerText: string;

  private roles: string[];
  authority: string;

  constructor(public token: TokenStorageService, public consoleService: ConsoleService,
              private configService: ConfigurationsService, private translocoService: TranslocoService, private utils: UtilService) {
    this.token = token;
    this.timerId = window.setInterval(() => {
      this.time = Date.now();
    }, 1000);
    this.translocoService.langChanges$.subscribe(newLanguage => {
      this.utils.update(Constants.BROWSER_KEY, Constants.LANGUAGE_KEY, newLanguage);
    });
  }

  ngOnInit(): void {
    this.performInit();
  }

  ngOnDestroy(): void {
    window.clearInterval(this.timerId);
  }

  logout() {
    this.token.signOut();
  }

  changeHeaderText(newHeaderText: string) {
    this.headerText = newHeaderText;
  }

  performInit() {
    this.configService.synchronisePreferences();
    this.consoleService.log('Performed init!');
  }

}
