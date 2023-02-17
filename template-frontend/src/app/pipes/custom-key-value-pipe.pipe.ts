import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'keyValuePipe'
})
export class CustomKeyValuePipePipe implements PipeTransform {

  transform(value: any, ...args: unknown[]): Array<any> {
    let array = [];
    if (value) {
      Object.keys(value).forEach(ix => {
          array.push({key: ix, value: value[ix]});
        }
      );
      return array;
    } else {
      console.error('could not build array for value', value);
      return [];
    }
  }

}
