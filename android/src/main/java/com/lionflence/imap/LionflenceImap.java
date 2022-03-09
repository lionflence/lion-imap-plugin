package com.lionflence.imap;

import android.util.Log;
import javax.mail.search.HeaderTerm;
import javax.security.auth.callback.Callback;
import org.json.JSONException;

import java.io.InputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Date;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Base64;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Long;
import java.lang.Integer;
import java.util.Collections;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Part;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SubjectTerm;
import javax.mail.search.BodyTerm;
import javax.mail.search.HeaderTerm;
import javax.mail.search.AndTerm;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Flags;
import javax.mail.Flags;
import javax.mail.Transport;

import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

public class LionflenceImap {
    private static Store store;
    private static Session session;
    private static HashMap<String, Long> paginationOffsets;

    public LionflenceImap() {
        try {
            if (store == null) {
                Properties props = System.getProperties();
                props.setProperty("mail.store.protocol", "imaps");

                session = Session.getDefaultInstance(props, null);
                store = session.getStore("imaps");
            }
            if(paginationOffsets == null) {
                paginationOffsets = new HashMap<String, Long>();
            }
        } catch(Exception ex) {

        }

    }

    public JSObject connect(PluginCall call) throws Exception {
        String host = call.getString("host");
        Integer port = (int) call.getInt("port");
        String user = call.getString("username");
        String password = call.getString("password");
        JSObject object = new JSObject();

        try {
            store.connect(host, port, user, password);
        } catch(Exception e) {
            if(!e.getMessage().contains("already connected")) {
                throw e;
            }
        }
        object.put("connected", true);
        return object;
    }

    public JSObject disconnect(PluginCall call) throws Exception {
        JSObject object = new JSObject();
        try {
            store.close();
            object.put("disconnected", true);
        } catch(Exception e) {
            object.put("disconnected", false);
        }

        return object;
    }

    public JSObject sendMessage(PluginCall call) throws Exception {
        JSObject object = new JSObject();
        String from = call.getString("from");
        String[] to = this.convertJSONArrayToStringArray(call.getArray("to"));
        String[] cc = this.convertJSONArrayToStringArray(call.getArray("cc"));
        String[] bcc = this.convertJSONArrayToStringArray(call.getArray("bcc"));
        String content = call.getString("content");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, "text/html; charset=utf-8");

        Message message = new MimeMessage(session);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);

        message.setFrom(new InternetAddress(from));
        for(int i = 0; i < to.length; i++) {
            message.addRecipient(Message.RecipientType.TO, InternetAddress.parse(to[i])[0]);
        }
        for(int i = 0; i < cc.length; i++) {
            message.addRecipient(Message.RecipientType.CC, InternetAddress.parse(cc[i])[0]);
        }
        for(int i = 0; i < bcc.length; i++) {
            message.addRecipient(Message.RecipientType.BCC, InternetAddress.parse(bcc[i])[0]);
        }
        try {
            Transport.send(message);
            object.put("sent", true);
        } catch(Exception e) {
            object.put("sent", false);
        }

        return object;
    }

    public JSObject isConnected(PluginCall call) throws Exception {
        JSObject object = new JSObject();
        try {
            object.put("connected", store.isConnected());
        } catch(Exception e) {
            object.put("connected", false);
        }

        return object;
    }

    public JSObject testConnection(PluginCall call) throws Exception {
        String host = call.getString("host");
        Integer port = (int) call.getInt("port");
        String user = call.getString("username");
        String password = call.getString("password");

        store.connect(host, port, user, password);
        return new JSObject();
    }

    public JSArray listMessagesHeadersByConsecutiveNumber(PluginCall call) throws Exception {
        String folderName = call.getString("folderName");
        Integer start = call.getInt("start");
        Integer end = call.getInt("end");
        Message[] messages = this.listMessageHeaders(folderName, start, end);

        JSArray resultData = this.parseMessagesHeaders(messages);
        return resultData;
    }

    public JSObject getMessageByMessageId(PluginCall call) throws Exception {
        String folderName = call.getString("folderName");
        String messageId = call.getString("messageId");

        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_ONLY);
        final SearchTerm headerTerm = new HeaderTerm("Message-ID", messageId);

        Message[] messages = emailFolder.search(headerTerm);

        if(messages.length == 1) {
            return this.parseFullMessage(messages[0], true);
        } else {
            return null;
        }
    }

    public JSArray getMessagesByHeader(PluginCall call) throws Exception {
        String headerName = call.getString("headerName");
        String headerValue = call.getString("headerValue");
        Integer limit = call.getInt("limit", 1);

        JSArray messages = new JSArray();

        JSArray folders = this.listMailFolders(call);

        Message[] msgs = this.getMessagesByHeader(headerName, headerValue, limit, folders);

        for(Message m: msgs) {
            messages.put(this.parseFullMessage(m, false));
        }
        return messages;
    }

    private Message[] getMessagesByHeader(String headerName, String headerValue, int limit, JSArray folders) throws Exception {
        ArrayList messages = new ArrayList<Message>();
        for(int i = 0; i < folders.length(); i++) {
            Folder emailFolder = store.getFolder(folders.getString(i));
            emailFolder.open(Folder.READ_ONLY);
            final SearchTerm headerTerm = new HeaderTerm(headerName, headerValue);

            Message[] msgs = emailFolder.search(headerTerm);
            for(Message m: msgs) {
                messages.add(m);
                if(messages.size() >= limit) {
                    emailFolder.close(false);
                    Message[] data = new Message[messages.size()];
                    messages.toArray(data);
                    return data;
                }
            }
            emailFolder.close(false);
        }
        Message[] data = new Message[messages.size()];
        messages.toArray(data);
        return data;
    }

    public JSArray searchMessages(PluginCall call) throws Exception {
        String query = call.getString("query");
        Integer page = call.getInt("page", 1);
        Integer limit = call.getInt("limit", 25);
        String folderName = call.getString("folderName");
        String currentPageKey = "p_" + folderName + "_" + query + "_" + page + "_" + limit;
        String lastPageKey = "p_" + folderName + "_" + query + "_" + (page - 1) + "_" + limit;

        Long startDate;

        if(paginationOffsets.containsKey(lastPageKey)) {
            startDate = paginationOffsets.get(lastPageKey);
        } else {
            startDate = (Long) (new Date()).getTime() + 24 * 60 * 60 * 1000;
        }


        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_ONLY);

        JSArray resultData = new JSArray();

        long from = startDate;
        long to = startDate - (long) (24 * 60 * 60) * 1000 * 28;
        Message[] messages = this.internalSearch(emailFolder, query, to, from);
        long oldest = 0;
        for(int j = (messages.length - 1); j >= 0 && resultData.length() < limit; j--) {
            if(messages[j].getReceivedDate().getTime() < oldest || oldest == 0) {
                oldest = messages[j].getReceivedDate().getTime();
            }
            resultData.put(this.parseMessagesHeader(messages[j]));
        }

        emailFolder.close(false);
        paginationOffsets.put(currentPageKey, oldest);
        return resultData;
    }

    public JSObject getThreadForMessage(PluginCall call) throws Exception {
        String messageId = call.getString("messageId");
        ArrayList<Message> messages = new ArrayList<Message>();
        JSArray folders = this.listMailFolders(call);
        this.recursivelyFetchMessages(messageId, messages, folders);

        JSArray msgs = new JSArray();
        for(Message m: messages) {
            msgs.put(this.parseFullMessage(m, false));
        }

        JSObject object = new JSObject();
        object.put("messages", msgs);
        return object;
    }

    private Message[] recursivelyFetchMessages(String messageId, ArrayList<Message> messages, JSArray folders) throws Exception {
        Message[] msgs = this.getMessagesByHeader("Message-ID", messageId, 1, folders);

        if(msgs.length == 1) {
            messages.add(msgs[0]);
            String inReplyTo = this.getMessageHeaderValue(msgs[0], "In-Reply-To");
            boolean alreadyInList = false;
            for(Message m: messages) {
                String msgId = this.getMessageHeaderValue(m, "Message-ID");
                if(msgId != null && inReplyTo != null && inReplyTo.equals(msgId)) {
                    alreadyInList = true;
                    break;
                }
            }
            if(!alreadyInList && inReplyTo != null) {
                this.recursivelyFetchMessages(inReplyTo, messages, folders);
            }
        }
        return msgs;
    }

    private Message[] internalSearch(Folder folder, String query, long minDateInMilliseconds , long maxDateInMilliseconds) throws Exception {
        final SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.valueOf("LT").getComparisonTerm(), new Date(maxDateInMilliseconds));
        final SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.valueOf("GT").getComparisonTerm(), new Date(minDateInMilliseconds));
        final SearchTerm andTerm = new AndTerm(olderThan, newerThan);
        if(!query.equals("")) {
            SearchTerm queryTerm = new OrTerm(new SubjectTerm(query), new BodyTerm(query));
            final SearchTerm and2Term = new AndTerm(andTerm, queryTerm);
            return folder.search(and2Term);
        } else {
            return folder.search(andTerm);
        }
    }

    public JSArray searchMessagesByDatePeriod(PluginCall call) throws Exception {
        String folderName = call.getString("folderName");
        int start = call.getInt("start");
        int end = call.getInt("end");

        Message[] messages = this.listMessageHeaders(folderName, start, end);

        JSArray resultData = new JSArray();

        for (Message m : messages) {
            resultData.put(m.getMessageNumber());
        }
        return resultData;
    }

    public JSObject copyToFolder(PluginCall call) throws Exception {
        String sourceFolder = call.getString("sourceFolder");
        String destinationFolder = call.getString("destinationFolder");
        int[] messageNums = this.convertJSONArrayToIntArray(call.getArray("messageIds"));

        Folder emailFolder = store.getFolder(sourceFolder);
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages(messageNums);
        emailFolder.copyMessages(messages, store.getFolder(destinationFolder));
        emailFolder.close(true);

        return new JSObject();
    }

    public JSObject deleteMessage(PluginCall call) throws Exception {
        return new JSObject();
    }

    public JSObject setFlag(PluginCall call) throws Exception {
        String folderName = call.getString("folderName");
        int[] messageNums = this.convertJSONArrayToIntArray(call.getArray("messageNums"));
        Flag flag = Flag.valueOf(call.getString("flag"));
        boolean status = call.getBoolean("status");
        JSObject resultData = new JSObject();
        JSArray modifiedMessages = new JSArray();

        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_WRITE);

        int totalMessagesInFolder = emailFolder.getMessageCount();

        Message[] messages = emailFolder.getMessages(
                Arrays.stream(messageNums).filter(num -> num <= totalMessagesInFolder).toArray()
        );

        for (Message message : messages) {
            message.setFlag(flag.getFlag(), status);
            modifiedMessages.put(message.getMessageNumber());
        }

        emailFolder.close(true);

        resultData.put("status", true);
        resultData.put("modified", modifiedMessages);

        return resultData;
    }

    public JSArray listMailFolders(PluginCall call) throws Exception {
        String pattern = call.getString("pattern");

        if(pattern == null || pattern.equals("")) {
            pattern = "*";
        }

        Folder[] folders = store.getDefaultFolder().list(pattern);

        JSArray resultData = new JSArray(Arrays.stream(folders).map(Folder::getFullName).toArray());
        return resultData;
    }

    public JSObject getFullMessageData(PluginCall call) throws Exception {
        String folderName = call.getString("folderName");
        Integer messageNumber = call.getInt("messageNumber");

        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_ONLY);

        Message message = emailFolder.getMessage(messageNumber);

        return this.parseFullMessage(message, true);
    }

    private Message[] listMessageHeaders(String folderName, int start, int end, String query) throws Exception {
        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_ONLY);
        return emailFolder.getMessages(start, end);
    }

    private Message[] listMessageHeaders(String folderName, long minDateInMilliseconds, long maxDateInMilliseconds) throws Exception {
        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_ONLY);
        final SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.valueOf("LT").getComparisonTerm(), new Date(maxDateInMilliseconds));
        final SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.valueOf("GT").getComparisonTerm(), new Date(minDateInMilliseconds));
        final SearchTerm andTerm = new AndTerm(olderThan, newerThan);
        return emailFolder.search(andTerm);
    }

    public JSObject getMessageAttachment(PluginCall call) throws Exception {
        return null;
    }

    private JSObject parseFullMessage(Message message, boolean withAttachments) throws MessagingException, JSONException, IOException {
        JSObject resultData = new JSObject();
        resultData.put("messageNumber", message.getMessageNumber());
        resultData.put("folder", message.getFolder() != null ? message.getFolder().toString() : "");
        resultData.put("from", this.parseAddressHeader(message.getFrom()));
        resultData.put("allRecipients", this.parseAddressHeader(message.getAllRecipients()));
        resultData.put("toRecipients", this.parseAddressHeader(message.getRecipients(Message.RecipientType.TO)));
        resultData.put("ccRecipients", this.parseAddressHeader(message.getRecipients(Message.RecipientType.CC)));
        resultData.put("bccRecipients", this.parseAddressHeader(message.getRecipients(Message.RecipientType.BCC)));
        resultData.put("replyTo", this.parseAddressHeader(message.getReplyTo()));
        resultData.put("sentDate", this.parseStringResult(message.getSentDate()));
        resultData.put("receivedDate", this.parseStringResult(message.getReceivedDate()));
        resultData.put("subject", this.parseStringResult(message.getSubject()));
        resultData.put("description", this.parseStringResult(message.getDescription()));
        resultData.put("fileName", this.parseStringResult(message.getFileName()));
        resultData.put("disposition", this.parseStringResult(message.getDisposition()));
        resultData.put("flags", this.serializeFlags(message.getFlags()));
        resultData.put("lineCount", message.getLineCount());
        resultData.put("allMessageHeaders", this.parseAllMessageHeaders(message));
        resultData.put("contentType", this.parseStringResult(message.getContentType()));
        resultData.put("bodyContent", this.getTextFromMimeMultipart(message.getContent(), message.getContentType()));
        resultData.put("size", message.getSize());

        return resultData;
    }

    private JSArray parseAddressHeader(Address[] address) {
        try {
            JSArray jsonArray = new JSArray();

            if (address != null) {
                for (Address a : address) {
                    JSObject jsonObject = new JSObject();

                    InternetAddress iAddress = (InternetAddress) a;

                    jsonObject.put("address", iAddress.getAddress());
                    jsonObject.put("personal", iAddress.getPersonal());
                    jsonObject.put("type", iAddress.getType());

                    jsonArray.put(jsonObject);
                }
            }

            return jsonArray;
        } catch (Exception e) {
            return new JSArray();
        }
    }

    private <T> String parseStringResult(T data) {
        try {
            if (data != null) {
                return data.toString();
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }

    private JSArray serializeFlags(Flags flags) {
        JSArray flagsArray = new JSArray();

        if (flags.contains(Flags.Flag.ANSWERED))
            flagsArray.put("Answered");
        if (flags.contains(Flags.Flag.DELETED))
            flagsArray.put("Deleted");
        if (flags.contains(Flags.Flag.DRAFT))
            flagsArray.put("Draft");
        if (flags.contains(Flags.Flag.FLAGGED))
            flagsArray.put("Flagged");
        if (flags.contains(Flags.Flag.RECENT))
            flagsArray.put("Recent");
        if (flags.contains(Flags.Flag.SEEN))
            flagsArray.put("Seen");
        if (flags.contains(Flags.Flag.USER))
            flagsArray.put("*");

        return flagsArray;
    }

    private JSObject parseAllMessageHeaders(Message messageData) {
        try {
            JSObject resultData = new JSObject();

            MimeMessage message = (MimeMessage) messageData;

            Enumeration allHeaders = message.getAllHeaders();
            while (allHeaders.hasMoreElements()) {
                Header header = (Header) allHeaders.nextElement();
                String headerName = header.getName();
                String headerVal = header.getValue();

                resultData.put(headerName, headerVal);
            }

            return resultData;
        } catch (Exception ex) {
            return new JSObject();
        }
    }

    private JSArray getTextFromMimeMultipart(Object body, String contentType) {
        try {
            JSArray fullContent = new JSArray();

            if (body.getClass().equals(String.class)) {
                JSObject contentData = new JSObject();

                contentData.put("type", contentType);
                contentData.put("content", body);

                fullContent.put(contentData);
            } else {
                MimeMultipart mimeMultipart = (MimeMultipart) body;

                int count = mimeMultipart.getCount();

                for (int i = 0; i < count; i++) {
                    JSObject contentData = new JSObject();

                    BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                    if (bodyPart.isMimeType("text/plain")) {
                        contentData.put("type", "text/plain");
                        contentData.put("content", bodyPart.getContent());
                    } else if (bodyPart.isMimeType("text/html")) {
                        contentData.put("type", "text/html");
                        contentData.put("content", bodyPart.getContent());
                    } else if (bodyPart.getContent() instanceof MimeMultipart) {
                        if (bodyPart.getContent() instanceof Multipart) {

                            Multipart multipart = (Multipart) body;

                            for (int j = 0; j < multipart.getCount(); j++) {
                                Part part = multipart.getBodyPart(j);
                                String disposition = part.getDisposition();

                                if ((disposition != null) &&
                                        ((disposition.equalsIgnoreCase(Part.ATTACHMENT) ||
                                                (disposition.equalsIgnoreCase(Part.INLINE))))) {
                                    MimeBodyPart mimeBodyPart = (MimeBodyPart) part;
                                    String fileName = mimeBodyPart.getFileName();

                                    contentData.put("type", mimeBodyPart.getContentType());
                                    contentData.put("fileName", fileName);
                                    contentData.put("content", mimeBodyPart);
                                }
                            }
                        }

                        fullContent = getTextFromMimeMultipart((Object) bodyPart.getContent(), contentType);
                    }

                    if (contentData.length() > 0) {
                        fullContent.put(contentData);
                    }
                }
            }

            return fullContent;
        } catch (Exception ex) {
            return new JSArray();
        }
    }

    public JSObject getMessageCountByFolderName(PluginCall call) throws Exception {
        String folderName = call.getString("folderName");
        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_ONLY);
        JSObject object = new JSObject();
        object.put("count", emailFolder.getMessageCount());
        return object;
    }

    private int[] convertJSONArrayToIntArray(JSArray array) throws Exception {
        if (array != null) {
            int[] numbers = new int[array.length()];

            for (int i = 0; i < array.length(); ++i) {
                numbers[i] = array.getInt(i);
            }

            return numbers;
        }

        return new int[]{};
    }

    private String[] convertJSONArrayToStringArray(JSArray array) throws Exception {
        if (array != null) {
            String[] strings = new String[array.length()];

            for (int i = 0; i < array.length(); ++i) {
                strings[i] = array.getString(i);
            }

            return strings;
        }

        return new String[]{};
    }

    private String getTextPreview(Object message) throws Exception {
        Object body = null;
        if(message instanceof Message) {
            body = ((Message) message).getContent();
        } else {
            body = (Multipart) message;
        }
        String text = "";
        try {
            if (body.getClass().equals(String.class)) {
                text = ((String) body);
            } else {
                MimeMultipart mimeMultipart = (MimeMultipart) body;

                int count = mimeMultipart.getCount();

                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                    if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
                        return this.sanitizeHtml(bodyPart.getContent().toString());
                    } else if (bodyPart.getContent() instanceof MimeMultipart) {
                        if (bodyPart.getContent() instanceof Multipart) {
                            Multipart multipart = (Multipart) body;
                        }

                        text = this.getTextPreview((Object) bodyPart.getContent());
                        if(text.length() > 0) {
                            return this.sanitizeHtml(text);
                        }
                    }
                }
            }

            return this.sanitizeHtml(text);
        } catch (Exception ex) {
            return "";
        }
    }

    private String sanitizeHtml(String html) {
        int start = html.indexOf("<head");
        while(start != -1) {
            int end = html.indexOf("</head>");
            if(end != -1) {
                html = html.substring(0, start) + html.substring(end + "</head>".length());
            }
            start = html.indexOf("<head");
        }
        start = html.indexOf("<script");
        while(start != -1) {
            int end = html.indexOf("</script>");
            if(end != -1) {
                html = html.substring(0, start) + html.substring(end + "</script>".length());
            }
            start = html.indexOf("<script");
        }
        start = html.indexOf("<style");
        while(start != -1) {
            int end = html.indexOf("</style>");
            if(end != -1) {
                html = html.substring(0, start) + html.substring(end + "</style>".length());
            }
            start = html.indexOf("<style");
        }
        start = html.indexOf("<!--");
        while(start != -1) {
            int end = html.indexOf("-->");
            if(end != -1) {
                html = html.substring(0, start) + html.substring(end + "-->".length());
            }
            start = html.indexOf("<!--");
        }
        html = html
                .replaceAll("\\<[^>]*>","")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&\\w{4};", "")
                .replaceAll("&#\\d+?;", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("  ", " ")
                .trim();

        return html;
    }

    private JSObject parseMessagesHeader(Message mes) throws MessagingException, JSONException, Exception {
        JSObject message = new JSObject();

        message.put("messageNumber", mes.getMessageNumber());
        message.put("previewText", this.getTextPreview(mes));
        message.put("folder", parseStringResult(mes.getFolder()));
        message.put("from", parseAddressHeader(mes.getFrom()));
        message.put("toRecipients", parseAddressHeader(mes.getRecipients(Message.RecipientType.TO)));
        message.put("ccRecipients", parseAddressHeader(mes.getRecipients(Message.RecipientType.CC)));
        message.put("bccRecipients", parseAddressHeader(mes.getRecipients(Message.RecipientType.BCC)));
        message.put("receivedDate", parseStringResult(mes.getReceivedDate()));
        message.put("subject", parseStringResult(mes.getSubject()));
        message.put("flags", serializeFlags(mes.getFlags()));

        Enumeration allHeaders = mes.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = (Header) allHeaders.nextElement();
            if(header.getName().toLowerCase().equals("message-id")) {
                message.put("messageId", header.getValue());
            }
        }

        return message;
    }

    private String getMessageHeaderValue(Message message, String headerName) throws Exception {
        if(!message.getFolder().isOpen()) {
            message.getFolder().open(Folder.READ_ONLY);
        }

        Enumeration allHeaders = message.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = (Header) allHeaders.nextElement();
            if(header.getName().equals(headerName)) {
                return header.getValue();
            }
        }
        return null;
    }

    private JSArray parseMessagesHeaders(Message[] messages) throws MessagingException, JSONException, Exception {
        JSArray resultData = new JSArray();

        for (Message mes : messages) {
            JSObject message = this.parseMessagesHeader(mes);

            resultData.put(message);
        }

        return resultData;
    }

}

enum Flag {
    ANSWERED(Flags.Flag.ANSWERED),
    DRAFT(Flags.Flag.DRAFT),
    FLAGGED(Flags.Flag.FLAGGED),
    RECENT(Flags.Flag.RECENT),
    SEEN(Flags.Flag.SEEN),
    USER(Flags.Flag.USER),
    DELETED(Flags.Flag.DELETED);

    private final Flags.Flag flag;

    Flag(Flags.Flag flag) {
        this.flag = flag;
    }

    public Flags.Flag getFlag() {
        return this.flag;
    }
}

enum ComparisonTerm {
    LE(ReceivedDateTerm.LE), // The less than or equal to operator.
    LT(ReceivedDateTerm.LT), // The less than operator.
    EQ(ReceivedDateTerm.EQ), // The equality operator.
    NE(ReceivedDateTerm.NE), // The not equal to operator.
    GT(ReceivedDateTerm.GT), // The greater than operator.
    GE(ReceivedDateTerm.GE); // The greater than or equal to operator.

    private final int comparisonTerm;

    ComparisonTerm(int term) {
        this.comparisonTerm = term;
    }

    public int getComparisonTerm() {
        return this.comparisonTerm;
    }
}