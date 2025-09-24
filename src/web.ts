import { WebPlugin } from '@capacitor/core';

import type { AbsoluteOrientationPlugin } from './definitions';

export class AbsoluteOrientationWeb extends WebPlugin implements AbsoluteOrientationPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
