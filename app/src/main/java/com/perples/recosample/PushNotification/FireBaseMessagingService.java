package com.perples.recosample.PushNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.perples.recosample.R;
import com.perples.recosample.TabActivity;

/**
 * Created by dahye on 2016-10-05.
 */
public class FireBaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG ="FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //sendPushNotification(remoteMessage.getData().get("message"));
        sendPushNotification(remoteMessage.getNotification().getBody());
    }

    private void sendPushNotification(String message){
        System.out.println("received message : " + message);
        Intent intent = new Intent(this, TabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("FreeLine")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL) //진동위해 필수
                //.setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE);

                /*.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{1, 100, 1})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)*/;
                /*.setContentTitle("FreeLine")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(000000255, 500, 2000)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent, true)
                .setVibrate(new long[]{1,1,1});*/

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire();

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
