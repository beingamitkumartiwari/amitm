package com.getfreerecharge.mpaisafreerecharge.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.activities.MainActivity;

/**
 * Created by DEVEN SINGH on 8/5/2015.
 */
public class NotificationAlert {

    public static final int notifyID = 9001;

    public static void sendNotification(Context context, String msg){
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mNotifyBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("mPaisa: Free Recharge")
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.ic_launcher);
        } else {
            mNotifyBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("mPaisa: Free Recharge")
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.notification_icon)
                    .setColor(context.getApplicationContext().getResources().getColor(R.color.accent));
        }
//        mNotifyBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle("Free Recharge Swipe")
//                .setContentText(msg)
//
//                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(msg);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}
