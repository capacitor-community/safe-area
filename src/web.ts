import { WebPlugin } from '@capacitor/core';

import type { SafeAreaPlugin } from './definitions';

export class SafeAreaWeb extends WebPlugin implements SafeAreaPlugin {
    
    setSystemBarStyle(_options: { style: 'light' | 'dark' }): Promise<void> {
        return Promise.resolve();
    }
    
}
