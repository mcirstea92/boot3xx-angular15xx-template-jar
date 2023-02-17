/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {Component, OnInit} from '@angular/core';
import {ServiceContainer} from '../../services/service-container';
import {TokenStorageService} from '../../services/auth/token-storage.service';
import {LangDefinition, translocoConfig} from "@ngneat/transloco";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  constructor(public services: ServiceContainer, public token: TokenStorageService) {
    services.console.clazz = 'HomePageComponent';
  }

  ngOnInit(): void {
    this.services.console.log('Current active language: ' + this.services.transloco.getActiveLang());
  }

}
