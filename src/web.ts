import { WebPlugin } from '@capacitor/core';

import type { SafeAreaPlugin } from './definitions';

export class SafeAreaWeb extends WebPlugin implements SafeAreaPlugin {
  async setSystemBarsStyle(): Promise<void> {
    this.unavailable('not available for web');
  }

  async showSystemBars(): Promise<void> {
    this.unavailable('not available for web');
  }

  async hideSystemBars(): Promise<void> {
    this.unavailable('not available for web');
  }
}
