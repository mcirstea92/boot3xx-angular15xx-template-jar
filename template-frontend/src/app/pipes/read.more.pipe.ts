import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'readMore'
})
export class ReadMorePipe implements PipeTransform {

  transform(text: any, length: number = 20, showAll: boolean = false, suffix: string = '...'): any {
    if (showAll) {
      return text;
    }
    if (text && text.length > length) {
      return text.substring(0, length) + suffix;
    }
    return text;
  }

}
