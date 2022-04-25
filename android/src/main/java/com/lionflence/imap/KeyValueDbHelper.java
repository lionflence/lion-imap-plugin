package com.lionflence.imap;

import android.database.sqlite.*;
import android.content.Context;
import android.database.Cursor;
import 	android.content.ContentValues;
import java.util.Date;
import java.sql.Timestamp;
import javax.mail.Message;
import javax.mail.Folder;

public class KeyValueDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "kvstore_msg.db";

    public KeyValueDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE kv_table (kv_key TEXT PRIMARY KEY, kv_value TEXT, max_age INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // only a cache database so recreate it on upgrade
        db.execSQL("DROP TABLE IF EXISTS kv_table");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void write(String key, String value, Integer ttl) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());



        SQLiteDatabase dbr = getReadableDatabase();
        String[] projection = {
                "kv_value"
        };

        String selection = "kv_key = ?";
        String[] selectionArgs = { key };

        Cursor cursor = dbr.query("kv_table", projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToNext()) {
            cursor.close();
            SQLiteDatabase dbw = getWritableDatabase();
            // existing entry, update it.
            ContentValues values = new ContentValues();
            values.put("kv_value", value);
            values.put("max_age", ts.getTime() + ttl * 1000);
            String where = "kv_key = ?";
            String[] args = { key };
            dbw.update("kv_table", values, where, args);
        } else {
            cursor.close();
            SQLiteDatabase dbw = getWritableDatabase();
            // new entry, create it.
            ContentValues values = new ContentValues();
            values.put("kv_key", key);
            values.put("kv_value", value);
            values.put("max_age", ts.getTime() + ttl * 1000);

            dbw.insert("kv_table", null, values);
        }

    }

    public void write(String key, String value) {
        write(key, value, 3600);
    }

    public String read(String key) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
            "kv_value"
        };

        String selection = "kv_key = ? AND max_age > ?";
        String[] selectionArgs = { key, String.valueOf(ts.getTime()) };

        Cursor cursor = db.query("kv_table", projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToNext()) {
            String value = cursor.getString(cursor.getColumnIndexOrThrow("kv_value"));
            cursor.close();
            return value;
        } else {
            cursor.close();
            return null;
        }
    }

    public boolean hasEntry(String key) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                "kv_value"
        };

        String selection = "kv_key = ? AND max_age > ?";
        String[] selectionArgs = { key, String.valueOf(ts.getTime()) };

        Cursor cursor = db.query("kv_table", projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void removeMessageFromPaginationEntries(Message message, Folder folder) {

    }

    public void removeFolderPaginationEntries(String folderName) {
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.delete("kv_table", "kv_key like ?", new String[] { "page_" + folderName + "%" });
    }

    public void removePaginationEntries() {
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.delete("kv_table", "kv_key like ?", new String[] { "page_%" });
    }
}