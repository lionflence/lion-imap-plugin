export interface ImapConfig {
  username: string;
  host: string;
  port: number;
  password: string;
}

export interface Message {
  messageNumber: number;
  messageId: string;
  subject: string;
  previewText: string;
  folder: string;
  from: string[];
  to: string[];
  received: Date;
  bodyContent: MessagePart[];
  allMessageHeaders: MessageHeaders;
}

export interface MessagePart {
  content: string;
  disposition: string;
  type: string;
  fileName: string;
}

export interface MessageHeaders {
  [x: string]: string;
}

export interface NewMessage {
    answerTo?: Message;
    content: string;
    subject: string;
    to: string[];
    from: string;
    cc: string[];
    bcc: string[];
    attachments: any[];
}

export interface LionflenceImapPlugin {
  connect(config: ImapConfig): Promise<{connected: boolean}>;

  isConnected(): Promise<{connected: boolean}>;

  disconnect(): Promise<{disconnected: boolean}>;

  listMailFolders(call: { pattern: string }): Promise<{ folders: string[]}>;

  getMessageCountByFolderName(call: { folderName: string }): Promise<{ count: number }>;

  getMessageByMessageId(call: { folderName: string, messageId: string }): Promise<{message: Message}>;

  getMessagesByHeader(call: { headerName: string, headerValue: string }): Promise<{messages: Message[]}>;

  getFullMessageData(call: {folderName: string, messageNumber: number}): Promise<{message: Message}>;

  copyToFolder(call: {sourceFolder: string, destinationFolder: string, messageNums: number[]}): Promise<boolean>;

  setFlag(call: {folderName: string, messageNums: number[], flag: string, status: string}): Promise<boolean>;
  
  listMessagesHeadersByConsecutiveNumber(call: {folderName: string, start: number, end: number, query?: string}): Promise<{messages: Message[]}>;

  searchMessages(call: { query: string, page: number, limit: number, folderName: string }): Promise<{messages: Message[]}>;
}
