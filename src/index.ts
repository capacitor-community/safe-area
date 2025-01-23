import { registerPlugin } from '@capacitor/core';

import type { SafeAreaPlugin } from './definitions';

const SafeArea = registerPlugin<SafeAreaPlugin>('SafeArea', {
  web: () => import('./web').then((m) => new m.SafeAreaWeb()),
});

function setProperty(position: 'top' | 'left' | 'bottom' | 'right') {
  if (typeof document !== 'undefined') {
    const rootStyle = document.querySelector<HTMLElement>(':root')?.style;

    const currentValue = rootStyle?.getPropertyValue(`--safe-area-inset-${position}`);

    if (!currentValue) {
      // If a value is already present,
      // we can assume we do not have to set an initial value.
      // Probably the native code has already set a property.
      // Otherwise the developer or another plugin has set this value.
      rootStyle?.setProperty(`--safe-area-inset-${position}`, `max(env(safe-area-inset-${position}), 0px)`);
    }
  }
}

/**
 * Set initial safe area values.
 * This makes sure `var(--safe-area-inset-*)` values can be used immediately and everywhere.
 * This method will be automatically called.
 *
 * Note for developers using SSR:
 * Only in an SSR environment this method will not necessarily be executed.
 * So if you're using this plugin in an SSR environment,
 * you should call this method as soon as `window.document` becomes available.
 */
function initialize(): void {
  setProperty('top');
  setProperty('left');
  setProperty('bottom');
  setProperty('right');
}

initialize();

export * from './definitions';
export { SafeArea, initialize };
