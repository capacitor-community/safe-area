import { WebPlugin } from '@capacitor/core';

import type { SafeAreaPlugin } from './definitions';

export class SafeAreaWeb extends WebPlugin implements SafeAreaPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
