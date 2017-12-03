package jbcu10.dev.medalert.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by ptyagi on 4/17/17.
 */

/**
 * AlarmReceiver handles the broadcast message and generates Notification
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");
        String uuid = intent.getExtras().getString("uuid");
        Log.e("MyActivity", "In the receiver with " + uuid);

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra("content", content);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("uuid", uuid);

        context.startService(serviceIntent);
    }

   /* public NotificationCompat.Builder buildLocalNotification(Context context, Intent intent,PendingIntent pendingIntent) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "M_CH_ID");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_alert)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(sound)
                .setTicker("Reminder")
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("content"))
                .setContentInfo(intent.getCharSequenceExtra("contentInfo"));

      return notificationBuilder;
    }*/


}
