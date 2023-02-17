import {Injectable} from '@angular/core';
import {ConsoleMessage} from '../../model/Containers';
import {MatDialog} from '@angular/material/dialog';
import {DialogComponent, DialogData} from '../../modules/dialog/dialog.component';
import {DatePipe} from '@angular/common';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConsoleService {
  private _collectedMessages: Array<ConsoleMessage> = [];
  private errorColor = '#FF0000';
  private warnColor = '#FFA500';
  private infoColor = '#4444FF';
  private debugColor = '#0000FF';
  private div;
  private _clazz;

  set clazz(className: string) {
    this._clazz = className;
  }

  constructor(private dialog: MatDialog, private datePipe: DatePipe) {
  }

  showLogs() {
    this.div = document.createElement('div');
    // delete old children of the console div

    if (this._collectedMessages.length > 0) {
      this._collectedMessages.sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
      // append logs from array
      for (let i = 0; i < this._collectedMessages.length; i++) {
        let crtConsoleMsg = this._collectedMessages[i];
        this._append(this.createMessage(crtConsoleMsg.message, crtConsoleMsg.color));
      }
      let dialogData = new DialogData();
      dialogData.title = 'Logs';
      dialogData.bodyContainerData = this.div.innerHTML;
      let dialogRef = this.dialog.open(DialogComponent, {data: dialogData});
      dialogRef.afterClosed().subscribe(() => {
        this.div = null;
      });
    }
  }

  private createMessage(msg, color) {
    // Sanitize the input
    const span = document.createElement('SPAN');
    if (color !== undefined) {
      span.style.color = color;
    }
    span.appendChild(document.createTextNode(msg));
    return span;
  }

  private _append(element) {
    if (this.div) {
      this.div.appendChild(element);
      this.div.appendChild(document.createElement('BR'));
    }
    // $(window).scrollTo('max', {duration: 500});
  }

  log(message, ...args: any) {
    message = this.format(message, args);
    console.log(message);
    this._append(this.createMessage(message, this.infoColor));
    this._collectedMessages.push(this.createConsoleMessage(this.infoColor, message));
  }

  info(message, ...args: any) {
    message = this.format(message, args);
    console.info(message);
    this._append(this.createMessage(message, this.infoColor));
    this._collectedMessages.push(this.createConsoleMessage(this.infoColor, message));
  }

  debug(message, ...args: any) {
    message = this.format(message, args);
    console.debug(message);
    this._append(this.createMessage(message, this.debugColor));
    this._collectedMessages.push(this.createConsoleMessage(this.debugColor, message));
  }

  warn(message, ...args: any) {
    message = this.format(message, args);
    console.warn(message);
    this._append(this.createMessage(message, this.warnColor));
    this._collectedMessages.push(this.createConsoleMessage(this.warnColor, message));
  }

  error(message: string, ...args: any) {
    message = this.format(message, args);
    console.error(message);
    this._append(this.createMessage(message, this.errorColor));
    this._collectedMessages.push(this.createConsoleMessage(this.errorColor, message));
  }

  createConsoleMessage(color: string, message: string) {
    return new ConsoleMessage(new Date(), color, message);
  }

  format(message, ...args: any) {
    if (!message) {
      return null;
    }
    args = args[0];
    if (!args || !args.length || args[0].length) {
      return `${this.datePipe.transform(new Date(), environment.dateTimeFormat)} ${this._clazz ? '[' + this._clazz + ']' : ''} - ${message}`;
    }

    let formatted = null;

    // replacement by object - {{prop}}
    if (!!(args[0].constructor && args[0].constructor.name.toLowerCase() === 'object')) {
      for (let i in args[0]) {
        let n = `{{${i}}}`;
        formatted = message.includes(n) ? message.replaceAll(n, JSON.stringify(args[0][i]) + '') : message;
      }
    }
    // replacement by placeholders - {}
    if (!formatted) {
      formatted = args.reduce((s, v) => s.replace('{}', JSON.stringify(v)), message);
    }
    return `${this.datePipe.transform(new Date(), environment.dateTimeFormat)} ${this._clazz ? '[' + this._clazz + ']' : ''} - ${formatted}`;
  }

  logAndCollectMessage(message: string, type: string, ...args: any) {
    switch (type) {
      case 'error':
        this.error(message, args);
        break;
      case 'warn':
        this.warn(message, args);
        break;
      case 'debug':
        this.debug(message, args);
        break;
      default:
        this.info(message, args);
        break;
    }
  }

  get collectedMessages() {
    return this._collectedMessages;
  }

  json(message: any, ...args) {
    if (args && args[0]) {
      this.log(message + JSON.stringify(args));
    } else {
      this.log(JSON.stringify(message));
    }
  }
}
