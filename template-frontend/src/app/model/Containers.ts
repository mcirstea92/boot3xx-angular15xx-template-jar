/*!
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {KeyValue} from '@angular/common';

export interface Event {
  id: number;
  description: string;
  type: string;
  createdTimestamp: number;
  lastUpdatedTimestamp: number;
  additionalInfo: string;
  ip: string;
  userId: number;
  createdString?: string;
}

export interface EventsContainer {
  events: Event[];
}

export interface EventContainer {
  event: Event;
}

export interface Config {
  id: number;
  category: string;
  created: number;
  name: string;
  type: number;
  user_id: number;
  value: string;
  version: number;
}

export interface ConfigsContainer {
  configs: Config[];
}

export interface ConfigContainer {
  config: Config;
}

export interface LogLevelContainer {
  logLevel: string;
}

export interface RootLogLevelContainer {
  rootLogLevel: string;
}

export interface LastLinesLogContainer {
  log: string[];
}

export interface InitHeader {
  initHeader(text: string): void;
}

export interface Properties {
  properties: KeyValue<string, any>[];
}

export interface Property {
  property: KeyValue<string, any>;
}

export interface RefreshContextResult {
  refresh: string[]; //list with keys from context
}

export interface RestartTomcatResult {
  restart: number;
}

export interface ChangedProperties {
  newProperties: KeyValue<string, any>[];
}

export interface ExecutionCommandResult {
  execution: string;
}

export enum NotificationType {
  ERROR = 'notification-error',
  WARN = 'notification-warn',
  INFO = 'notification-info',
  DEFAULT = 'notification-default',
  SUCCESS = 'notification-success'
}

export class ConsoleMessage {

  constructor(public timestamp: Date, public color: string, public message: string) {
  }

}

export enum SortMode {
  ASC = 'asc', DESC = 'desc'
}

export const DATE_FORMAT = 'dd-MM-yyyy';
export const DATETIME_FORMAT = 'dd-MM-yyyy HH:mm:ss';
export const TIME_FORMAT = 'HH:mm:ss';

export function getKeyByValue<T extends { [index: string]: string }>(myEnum: T, enumValue: string): keyof T | null {
  let keys = Object.keys(myEnum).filter(x => myEnum[x] == enumValue);
  return keys.length > 0 ? keys[0] : null;
}

export function getValueByKey<T extends { [index: string]: string }>(myEnum: T, enumKey: string): keyof T | null {
  return myEnum[enumKey];
}

export function getIndexByKey<T extends { [index: string]: string }>(myEnum: T, enumKey: string): number {
  return Object.keys(myEnum).indexOf(enumKey);
}

export function getIndexByValue<T extends { [index: string]: string }>(myEnum: T, enumValue: string): keyof T | null {
  let key = getKeyByValue(myEnum, enumValue);
  return getIndexByKey(myEnum, key.toString());
}

export function getEnumByValue<T>(myEnum: T, val: string): T {
  return myEnum[val];
}

export function getValueByIndex<T>(myEnum: T, index: number): string {
  return myEnum[Object.keys(myEnum)[index]];
}

export function getKeyByIndex<T>(myEnum: T, index: number): string {
  return Object.keys(myEnum)[index];
}
