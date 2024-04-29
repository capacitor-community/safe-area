import { registerPlugin } from '@capacitor/core';

import type { SafeAreaPlugin } from './definitions';

const SafeArea = registerPlugin<SafeAreaPlugin>('SafeArea', {
  web: () => import('./web').then(m => new m.SafeAreaWeb()),
});

function setProperty(position: 'top' | 'left' | 'bottom' | 'right') {
  document
    .querySelector<HTMLElement>(':root')
    ?.style.setProperty(
      `--safe-area-inset-${position}`,
      `max(env(safe-area-inset-${position}), 0px)`,
    );
}

/**
 * Set initial safe area values
 * This makes sure `var(--safe-area-inset-*)` values can be used immediately and everywhere
 */
setProperty('top');
setProperty('left');
setProperty('bottom');
setProperty('right');

export * from './definitions';
export { SafeArea };
