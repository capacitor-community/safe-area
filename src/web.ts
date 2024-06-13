import { WebPlugin } from '@capacitor/core';

import type { Config, SafeAreaPlugin } from './definitions';

export class SafeAreaWeb extends WebPlugin implements SafeAreaPlugin {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async enable(_options: { config: Config }): Promise<void> {
    return;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async disable(_options: { config: Config }): Promise<void> {
    return;
  }
}
