import { registerPlugin } from '@capacitor/core';

import type { LionflenceImapPlugin } from './definitions';

const LionflenceImap = registerPlugin<LionflenceImapPlugin>('LionflenceImap', {
  web: () => import('./web').then(m => new m.LionflenceImapWeb()),
});

export * from './definitions';
export { LionflenceImap };
