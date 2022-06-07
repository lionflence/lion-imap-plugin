export interface ImapConfig {
  username: string;
  imapHost: string;
  smtpHost: string;
  imapPort: number;
  smtpPort: number;
  password: string;
}

export interface Message {
  messageNumber: number;
  messageId: string;
  subject: string;
  previewText: string;
  folder: string;
  from: MessageAddress[];
  toRecipients: MessageAddress[];
  receivedDate: Date;
  sentDate: Date;
  bodyContent: MessagePart[];
  allMessageHeaders: MessageHeaders;
  [x: string]: any;
  attachments: MessageAttachment[];
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

export interface MessageAttachment {
    content: string,
    fileName: string,
    type: string;
    size?: number;
}

export interface MessageAddress {
    address: string;
    personal: string;
    type: string;
}

export interface NewMessage {
    answerTo?: Message;
    content: string;
    subject: string;
    to: MessageAddress[];
    from: string;
    cc: MessageAddress[];
    bcc: MessageAddress[];
    attachments: MessageAttachment[];
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

  sendMessage(call: { content: string, subject: string, from: string, to: MessageAddress[], cc: MessageAddress[], bcc: MessageAddress[], attachments: any[] }): Promise<{sent: boolean}>;

  getThreadForMessage(call: { messageId: string }): Promise<{messages: Message[] }>;

  getAttachmentContent(call: { messageId: string, folderName: string, offset: number }): Promise<{ content: string }>;

  deleteMessage(call: { messageId: string }): Promise<{ deleted: boolean }>;

  moveMessage(call: { messageId: string, folderName: string }): Promise<{ moved: boolean }>;
}
