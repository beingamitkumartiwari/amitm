package com.getfreerecharge.mpaisafreerecharge.gcm_work;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import com.getfreerecharge.mpaisafreerecharge.storage.MpFr_Database;
import com.getfreerecharge.mpaisafreerecharge.utils.NotificationAlert;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GCMNotificationIntentService extends IntentService {

    private MpFr_Database mpFr_database;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        mpFr_database=new MpFr_Database(GCMNotificationIntentService.this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                NotificationAlert.sendNotification(getApplicationContext(),"Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                NotificationAlert.sendNotification(getApplicationContext(),"Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                NotificationAlert.sendNotification(getApplicationContext(),extras.getString("message"));
                if(mpFr_database.getMessageCount()<100){
                    mpFr_database.insertMessage(extras.getString("message"),System.currentTimeMillis(),"unread");
                }else{
                    mpFr_database.updateMessage(extras.getString("message"),System.currentTimeMillis(),"unread");
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
