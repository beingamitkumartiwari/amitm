package com.getfreerecharge.mpaisafreerecharge.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.getfreerecharge.mpaisafreerecharge.pojos.NotificationItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by DEVEN SINGH on 7/3/2015.
 */
public class MpFr_Database extends SQLiteOpenHelper {

    public static final String MpFr_DATABASE = "MpFrDatabse.db";
    public static final String MpFr_TABLE_NAME = "alerts";
    public static final String GCM_MESSAGE = "message";
    public static final String ALERT_TIME_STAMP = "time_stamp";
    public static final String MSG_READ_STATUS = "read_status";

    public MpFr_Database(Context context) {
        super(context, MpFr_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table alerts " +
                        "(message TEXT,time_stamp INT8,read_status TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alerts");
        onCreate(db);
    }

    public boolean deleteAllNotification() {
        boolean isDeleted = false;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(MpFr_TABLE_NAME, null, null);
            database.close();
            isDeleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public void insertMessage(String msg, long timeStamp, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GCM_MESSAGE, msg);
        values.put(ALERT_TIME_STAMP, timeStamp);
        values.put(MSG_READ_STATUS, status);
        database.insert(MpFr_TABLE_NAME, null, values);
        database.close();
    }

    public int getMessageCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MpFr_TABLE_NAME);
        return numRows;
    }

    public void updateMessage(String msg, long timeStamp, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GCM_MESSAGE, msg);
        values.put(ALERT_TIME_STAMP, timeStamp);
        values.put(MSG_READ_STATUS, status);
        database.update(MpFr_TABLE_NAME, values, "time_stamp=(SELECT MIN(time_stamp) From alerts)", null);
        database.close();
    }

    public void updateMsgReadStatus() {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MSG_READ_STATUS, "read");
        database.update(MpFr_TABLE_NAME, values, null, null);
        database.close();
    }

//    public ArrayList<String> getAlertMessages() {
//        ArrayList<String> arrayList = new ArrayList<>();
//        String selectetionQuery = "SELECT * FROM " + FRS_TABLE_NAME;
//        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor cursor = database.rawQuery(selectetionQuery, null);
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            arrayList.add(cursor.getString(cursor.getColumnIndex(GCM_MESSAGE)));
//            cursor.moveToNext();
//        }
//        database.close();
//        return arrayList;
//    }

    public ArrayList<NotificationItem> getNotificationData() {
        ArrayList<NotificationItem> listOfData = new ArrayList<NotificationItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from alerts", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            NotificationItem notificationItem = new NotificationItem();
            notificationItem.setMessage(res.getString(res.getColumnIndex(GCM_MESSAGE)));
            notificationItem.setMsgReadStatus(res.getString(res.getColumnIndex(MSG_READ_STATUS)));
            notificationItem.setTiming(String.valueOf(res.getInt(res.getColumnIndex(ALERT_TIME_STAMP))));
            listOfData.add(notificationItem);
            res.moveToNext();
        }
        db.close();
        Collections.reverse(listOfData);
        return listOfData;
    }

    public int unreadMessagesCount() {
        int count = 0;
        String selectetionQuery = "SELECT * FROM " + MpFr_TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectetionQuery, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getString(cursor.getColumnIndex(MSG_READ_STATUS)).equals("unread")) {
                count++;
            }
            cursor.moveToNext();
        }
        database.close();
        return count;
    }
}
