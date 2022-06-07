# lion-imap-plugin

IMAP functionality for capacitor applications. Currently supports android only with JavaMail 1.6.6.
iOS support to come!

## Install

```bash
npm install @lionflence/lion-imap-plugin
npx cap sync
```


### Android
You will probably receive an error during the android compilation process telling you the following:

`2 files found with path 'META-INF/NOTICE.md' from inputs: ...`

You can fix this by excluding the files inside of the android packaging options like so:

Add the following inside your `android/app/build.gradle` file (NOTE: NOT `android/build.gradle`!):

```
android {
    ...
    packagingOptions {
        exclude 'META-INF/NOTICE.md'
        exclude 'META-INF/LICENSE.md'
    }
}
```

### iOS

Nothing yet

## Create and publish a new version


## API

<docgen-index>

* [`connect(...)`](#connect)
* [`isConnected()`](#isconnected)
* [`disconnect()`](#disconnect)
* [`listMailFolders(...)`](#listmailfolders)
* [`getMessageCountByFolderName(...)`](#getmessagecountbyfoldername)
* [`getMessageByMessageId(...)`](#getmessagebymessageid)
* [`getMessagesByHeader(...)`](#getmessagesbyheader)
* [`getFullMessageData(...)`](#getfullmessagedata)
* [`copyToFolder(...)`](#copytofolder)
* [`setFlag(...)`](#setflag)
* [`listMessagesHeadersByConsecutiveNumber(...)`](#listmessagesheadersbyconsecutivenumber)
* [`searchMessages(...)`](#searchmessages)
* [`sendMessage(...)`](#sendmessage)
* [`getThreadForMessage(...)`](#getthreadformessage)
* [`getAttachmentContent(...)`](#getattachmentcontent)
* [`deleteMessage(...)`](#deletemessage)
* [`moveMessage(...)`](#movemessage)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### connect(...)

```typescript
connect(config: ImapConfig) => Promise<{ connected: boolean; }>
```

| Param        | Type                                              |
| ------------ | ------------------------------------------------- |
| **`config`** | <code><a href="#imapconfig">ImapConfig</a></code> |

**Returns:** <code>Promise&lt;{ connected: boolean; }&gt;</code>

--------------------


### isConnected()

```typescript
isConnected() => Promise<{ connected: boolean; }>
```

**Returns:** <code>Promise&lt;{ connected: boolean; }&gt;</code>

--------------------


### disconnect()

```typescript
disconnect() => Promise<{ disconnected: boolean; }>
```

**Returns:** <code>Promise&lt;{ disconnected: boolean; }&gt;</code>

--------------------


### listMailFolders(...)

```typescript
listMailFolders(call: { pattern: string; }) => Promise<{ folders: string[]; }>
```

| Param      | Type                              |
| ---------- | --------------------------------- |
| **`call`** | <code>{ pattern: string; }</code> |

**Returns:** <code>Promise&lt;{ folders: string[]; }&gt;</code>

--------------------


### getMessageCountByFolderName(...)

```typescript
getMessageCountByFolderName(call: { folderName: string; }) => Promise<{ count: number; }>
```

| Param      | Type                                 |
| ---------- | ------------------------------------ |
| **`call`** | <code>{ folderName: string; }</code> |

**Returns:** <code>Promise&lt;{ count: number; }&gt;</code>

--------------------


### getMessageByMessageId(...)

```typescript
getMessageByMessageId(call: { folderName: string; messageId: string; }) => Promise<{ message: Message; }>
```

| Param      | Type                                                    |
| ---------- | ------------------------------------------------------- |
| **`call`** | <code>{ folderName: string; messageId: string; }</code> |

**Returns:** <code>Promise&lt;{ message: <a href="#message">Message</a>; }&gt;</code>

--------------------


### getMessagesByHeader(...)

```typescript
getMessagesByHeader(call: { headerName: string; headerValue: string; }) => Promise<{ messages: Message[]; }>
```

| Param      | Type                                                      |
| ---------- | --------------------------------------------------------- |
| **`call`** | <code>{ headerName: string; headerValue: string; }</code> |

**Returns:** <code>Promise&lt;{ messages: Message[]; }&gt;</code>

--------------------


### getFullMessageData(...)

```typescript
getFullMessageData(call: { folderName: string; messageNumber: number; }) => Promise<{ message: Message; }>
```

| Param      | Type                                                        |
| ---------- | ----------------------------------------------------------- |
| **`call`** | <code>{ folderName: string; messageNumber: number; }</code> |

**Returns:** <code>Promise&lt;{ message: <a href="#message">Message</a>; }&gt;</code>

--------------------


### copyToFolder(...)

```typescript
copyToFolder(call: { sourceFolder: string; destinationFolder: string; messageNums: number[]; }) => Promise<boolean>
```

| Param      | Type                                                                                     |
| ---------- | ---------------------------------------------------------------------------------------- |
| **`call`** | <code>{ sourceFolder: string; destinationFolder: string; messageNums: number[]; }</code> |

**Returns:** <code>Promise&lt;boolean&gt;</code>

--------------------


### setFlag(...)

```typescript
setFlag(call: { folderName: string; messageNums: number[]; flag: string; status: string; }) => Promise<boolean>
```

| Param      | Type                                                                                      |
| ---------- | ----------------------------------------------------------------------------------------- |
| **`call`** | <code>{ folderName: string; messageNums: number[]; flag: string; status: string; }</code> |

**Returns:** <code>Promise&lt;boolean&gt;</code>

--------------------


### listMessagesHeadersByConsecutiveNumber(...)

```typescript
listMessagesHeadersByConsecutiveNumber(call: { folderName: string; start: number; end: number; query?: string; }) => Promise<{ messages: Message[]; }>
```

| Param      | Type                                                                             |
| ---------- | -------------------------------------------------------------------------------- |
| **`call`** | <code>{ folderName: string; start: number; end: number; query?: string; }</code> |

**Returns:** <code>Promise&lt;{ messages: Message[]; }&gt;</code>

--------------------


### searchMessages(...)

```typescript
searchMessages(call: { query: string; page: number; limit: number; folderName: string; }) => Promise<{ messages: Message[]; }>
```

| Param      | Type                                                                             |
| ---------- | -------------------------------------------------------------------------------- |
| **`call`** | <code>{ query: string; page: number; limit: number; folderName: string; }</code> |

**Returns:** <code>Promise&lt;{ messages: Message[]; }&gt;</code>

--------------------


### sendMessage(...)

```typescript
sendMessage(call: { content: string; subject: string; from: string; to: MessageAddress[]; cc: MessageAddress[]; bcc: MessageAddress[]; attachments: any[]; }) => Promise<{ sent: boolean; }>
```

| Param      | Type                                                                                                                                                    |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`call`** | <code>{ content: string; subject: string; from: string; to: MessageAddress[]; cc: MessageAddress[]; bcc: MessageAddress[]; attachments: any[]; }</code> |

**Returns:** <code>Promise&lt;{ sent: boolean; }&gt;</code>

--------------------


### getThreadForMessage(...)

```typescript
getThreadForMessage(call: { messageId: string; }) => Promise<{ messages: Message[]; }>
```

| Param      | Type                                |
| ---------- | ----------------------------------- |
| **`call`** | <code>{ messageId: string; }</code> |

**Returns:** <code>Promise&lt;{ messages: Message[]; }&gt;</code>

--------------------


### getAttachmentContent(...)

```typescript
getAttachmentContent(call: { messageId: string; folderName: string; offset: number; }) => Promise<{ content: string; }>
```

| Param      | Type                                                                    |
| ---------- | ----------------------------------------------------------------------- |
| **`call`** | <code>{ messageId: string; folderName: string; offset: number; }</code> |

**Returns:** <code>Promise&lt;{ content: string; }&gt;</code>

--------------------


### deleteMessage(...)

```typescript
deleteMessage(call: { messageId: string; }) => Promise<{ deleted: boolean; }>
```

| Param      | Type                                |
| ---------- | ----------------------------------- |
| **`call`** | <code>{ messageId: string; }</code> |

**Returns:** <code>Promise&lt;{ deleted: boolean; }&gt;</code>

--------------------


### moveMessage(...)

```typescript
moveMessage(call: { messageId: string; folderName: string; }) => Promise<{ moved: boolean; }>
```

| Param      | Type                                                    |
| ---------- | ------------------------------------------------------- |
| **`call`** | <code>{ messageId: string; folderName: string; }</code> |

**Returns:** <code>Promise&lt;{ moved: boolean; }&gt;</code>

--------------------


### Interfaces


#### ImapConfig

| Prop           | Type                |
| -------------- | ------------------- |
| **`username`** | <code>string</code> |
| **`imapHost`** | <code>string</code> |
| **`smtpHost`** | <code>string</code> |
| **`imapPort`** | <code>number</code> |
| **`smtpPort`** | <code>number</code> |
| **`password`** | <code>string</code> |


#### Message

| Prop                    | Type                                                      |
| ----------------------- | --------------------------------------------------------- |
| **`messageNumber`**     | <code>number</code>                                       |
| **`messageId`**         | <code>string</code>                                       |
| **`subject`**           | <code>string</code>                                       |
| **`previewText`**       | <code>string</code>                                       |
| **`folder`**            | <code>string</code>                                       |
| **`from`**              | <code>MessageAddress[]</code>                             |
| **`toRecipients`**      | <code>MessageAddress[]</code>                             |
| **`receivedDate`**      | <code><a href="#date">Date</a></code>                     |
| **`sentDate`**          | <code><a href="#date">Date</a></code>                     |
| **`bodyContent`**       | <code>MessagePart[]</code>                                |
| **`allMessageHeaders`** | <code><a href="#messageheaders">MessageHeaders</a></code> |
| **`attachments`**       | <code>MessageAttachment[]</code>                          |


#### MessageAddress

| Prop           | Type                |
| -------------- | ------------------- |
| **`address`**  | <code>string</code> |
| **`personal`** | <code>string</code> |
| **`type`**     | <code>string</code> |


#### Date

Enables basic storage and retrieval of dates and times.

| Method                 | Signature                                                                                                    | Description                                                                                                                             |
| ---------------------- | ------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------- |
| **toString**           | () =&gt; string                                                                                              | Returns a string representation of a date. The format of the string depends on the locale.                                              |
| **toDateString**       | () =&gt; string                                                                                              | Returns a date as a string value.                                                                                                       |
| **toTimeString**       | () =&gt; string                                                                                              | Returns a time as a string value.                                                                                                       |
| **toLocaleString**     | () =&gt; string                                                                                              | Returns a value as a string value appropriate to the host environment's current locale.                                                 |
| **toLocaleDateString** | () =&gt; string                                                                                              | Returns a date as a string value appropriate to the host environment's current locale.                                                  |
| **toLocaleTimeString** | () =&gt; string                                                                                              | Returns a time as a string value appropriate to the host environment's current locale.                                                  |
| **valueOf**            | () =&gt; number                                                                                              | Returns the stored time value in milliseconds since midnight, January 1, 1970 UTC.                                                      |
| **getTime**            | () =&gt; number                                                                                              | Gets the time value in milliseconds.                                                                                                    |
| **getFullYear**        | () =&gt; number                                                                                              | Gets the year, using local time.                                                                                                        |
| **getUTCFullYear**     | () =&gt; number                                                                                              | Gets the year using Universal Coordinated Time (UTC).                                                                                   |
| **getMonth**           | () =&gt; number                                                                                              | Gets the month, using local time.                                                                                                       |
| **getUTCMonth**        | () =&gt; number                                                                                              | Gets the month of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                             |
| **getDate**            | () =&gt; number                                                                                              | Gets the day-of-the-month, using local time.                                                                                            |
| **getUTCDate**         | () =&gt; number                                                                                              | Gets the day-of-the-month, using Universal Coordinated Time (UTC).                                                                      |
| **getDay**             | () =&gt; number                                                                                              | Gets the day of the week, using local time.                                                                                             |
| **getUTCDay**          | () =&gt; number                                                                                              | Gets the day of the week using Universal Coordinated Time (UTC).                                                                        |
| **getHours**           | () =&gt; number                                                                                              | Gets the hours in a date, using local time.                                                                                             |
| **getUTCHours**        | () =&gt; number                                                                                              | Gets the hours value in a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                       |
| **getMinutes**         | () =&gt; number                                                                                              | Gets the minutes of a <a href="#date">Date</a> object, using local time.                                                                |
| **getUTCMinutes**      | () =&gt; number                                                                                              | Gets the minutes of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                           |
| **getSeconds**         | () =&gt; number                                                                                              | Gets the seconds of a <a href="#date">Date</a> object, using local time.                                                                |
| **getUTCSeconds**      | () =&gt; number                                                                                              | Gets the seconds of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                           |
| **getMilliseconds**    | () =&gt; number                                                                                              | Gets the milliseconds of a <a href="#date">Date</a>, using local time.                                                                  |
| **getUTCMilliseconds** | () =&gt; number                                                                                              | Gets the milliseconds of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                      |
| **getTimezoneOffset**  | () =&gt; number                                                                                              | Gets the difference in minutes between the time on the local computer and Universal Coordinated Time (UTC).                             |
| **setTime**            | (time: number) =&gt; number                                                                                  | Sets the date and time value in the <a href="#date">Date</a> object.                                                                    |
| **setMilliseconds**    | (ms: number) =&gt; number                                                                                    | Sets the milliseconds value in the <a href="#date">Date</a> object using local time.                                                    |
| **setUTCMilliseconds** | (ms: number) =&gt; number                                                                                    | Sets the milliseconds value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                              |
| **setSeconds**         | (sec: number, ms?: number \| undefined) =&gt; number                                                         | Sets the seconds value in the <a href="#date">Date</a> object using local time.                                                         |
| **setUTCSeconds**      | (sec: number, ms?: number \| undefined) =&gt; number                                                         | Sets the seconds value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                   |
| **setMinutes**         | (min: number, sec?: number \| undefined, ms?: number \| undefined) =&gt; number                              | Sets the minutes value in the <a href="#date">Date</a> object using local time.                                                         |
| **setUTCMinutes**      | (min: number, sec?: number \| undefined, ms?: number \| undefined) =&gt; number                              | Sets the minutes value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                   |
| **setHours**           | (hours: number, min?: number \| undefined, sec?: number \| undefined, ms?: number \| undefined) =&gt; number | Sets the hour value in the <a href="#date">Date</a> object using local time.                                                            |
| **setUTCHours**        | (hours: number, min?: number \| undefined, sec?: number \| undefined, ms?: number \| undefined) =&gt; number | Sets the hours value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                     |
| **setDate**            | (date: number) =&gt; number                                                                                  | Sets the numeric day-of-the-month value of the <a href="#date">Date</a> object using local time.                                        |
| **setUTCDate**         | (date: number) =&gt; number                                                                                  | Sets the numeric day of the month in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                        |
| **setMonth**           | (month: number, date?: number \| undefined) =&gt; number                                                     | Sets the month value in the <a href="#date">Date</a> object using local time.                                                           |
| **setUTCMonth**        | (month: number, date?: number \| undefined) =&gt; number                                                     | Sets the month value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                     |
| **setFullYear**        | (year: number, month?: number \| undefined, date?: number \| undefined) =&gt; number                         | Sets the year of the <a href="#date">Date</a> object using local time.                                                                  |
| **setUTCFullYear**     | (year: number, month?: number \| undefined, date?: number \| undefined) =&gt; number                         | Sets the year value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                      |
| **toUTCString**        | () =&gt; string                                                                                              | Returns a date converted to a string using Universal Coordinated Time (UTC).                                                            |
| **toISOString**        | () =&gt; string                                                                                              | Returns a date as a string value in ISO format.                                                                                         |
| **toJSON**             | (key?: any) =&gt; string                                                                                     | Used by the JSON.stringify method to enable the transformation of an object's data for JavaScript Object Notation (JSON) serialization. |


#### MessagePart

| Prop              | Type                |
| ----------------- | ------------------- |
| **`content`**     | <code>string</code> |
| **`disposition`** | <code>string</code> |
| **`type`**        | <code>string</code> |
| **`fileName`**    | <code>string</code> |


#### MessageHeaders


#### MessageAttachment

| Prop           | Type                |
| -------------- | ------------------- |
| **`content`**  | <code>string</code> |
| **`fileName`** | <code>string</code> |
| **`type`**     | <code>string</code> |
| **`size`**     | <code>number</code> |

</docgen-api>
