import { registerPlugin } from '@capacitor/core';

import type { AbsoluteOrientationPlugin } from './definitions';

const AbsoluteOrientation = registerPlugin<AbsoluteOrientationPlugin>('AbsoluteOrientation', {
	web: () => import('./web').then((m) => new m.AbsoluteOrientationWeb())
});

export * from './definitions';
export { AbsoluteOrientation };
