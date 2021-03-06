package com.lionflence.imap;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.content.Context;

@CapacitorPlugin(name = "LionflenceImap")
public class LionflenceImapPlugin extends Plugin {

    private LionflenceImap implementation;
    private Context context;

    public void load() {
        context = getContext();
        implementation = new LionflenceImap(context);
    }

    @PluginMethod
    public void connect(PluginCall call) {
        try {
            call.resolve(implementation.connect(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void disconnect(PluginCall call) {
        try {
            call.resolve(implementation.disconnect(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void isConnected(PluginCall call) {
        try {
            call.resolve(implementation.isConnected(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void sendMessage(PluginCall call) {
        try {
            call.resolve(implementation.sendMessage(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void getThreadForMessage(PluginCall call) {
        try {
            call.resolve(implementation.getThreadForMessage(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void listMailFolders(PluginCall call) {
        try {
            JSObject object = new JSObject();
            object.put("folders", implementation.listMailFolders(call));
            call.resolve(object);
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void getMessageCountByFolderName(PluginCall call) {
        try {
            call.resolve(implementation.getMessageCountByFolderName(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void searchMessagesByDatePeriod(PluginCall call) {
        try {
            JSObject object = new JSObject();
            object.put("messages", implementation.searchMessagesByDatePeriod(call));
            call.resolve(object);
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void getFullMessageData(PluginCall call) {
        try {
            JSObject object = new JSObject();
            object.put("message", implementation.getFullMessageData(call));
            call.resolve(object);
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void getMessagesByHeader(PluginCall call) {
        try {
            JSObject object = new JSObject();
            object.put("messages", implementation.getMessagesByHeader(call));
            call.resolve(object);
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void searchMessages(PluginCall call) {
        try {
            JSObject object = new JSObject();
            object.put("messages", implementation.searchMessages(call));
            call.resolve(object);
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void testConnection(PluginCall call) {
        try {
            call.resolve(implementation.testConnection(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void listMessagesHeadersByConsecutiveNumber(PluginCall call) {
        try {
            JSObject object = new JSObject();
            object.put("messages", implementation.listMessagesHeadersByConsecutiveNumber(call));
            call.resolve(object);
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void getMessageByMessageId(PluginCall call) {
        try {
            call.resolve(implementation.getMessageByMessageId(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void copyToFolder(PluginCall call) {
        try {
            call.resolve(implementation.copyToFolder(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void deleteMessage(PluginCall call) {
        try {
            call.resolve(implementation.deleteMessage(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void moveMessage(PluginCall call) {
        try {
            call.resolve(implementation.moveMessage(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void setFlag(PluginCall call) {
        try {
            call.resolve(implementation.setFlag(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }

    @PluginMethod
    public void getAttachmentContent(PluginCall call) {
        try {
            call.resolve(implementation.getAttachmentContent(call));
        } catch(Exception e) {
            call.errorCallback(e.getMessage());
        }
    }
}
