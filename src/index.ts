import { registerPlugin } from '@capacitor/core';

import type { LionflenceImapPlugin, MessageHeaders, ImapConfig, Message, MessagePart, NewMessage } from './definitions';

const LionflenceImap = registerPlugin<LionflenceImapPlugin>('LionflenceImap', {
});

export * from './definitions';
export { LionflenceImap, MessageHeaders, ImapConfig, Message, MessagePart, NewMessage };
