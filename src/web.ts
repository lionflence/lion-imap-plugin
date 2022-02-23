import { WebPlugin } from '@capacitor/core';

import type { LionflenceImapPlugin } from './definitions';

export class LionflenceImapWeb
  extends WebPlugin
  implements LionflenceImapPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
