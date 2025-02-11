package ar.com.anura.plugins.phonecallnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PushNotificationReceiver extends BroadcastReceiver {
  private static final String TAG = "PushNotificationReceiver";

  private static final String CHANNEL_ID = "example_channel_id";
  private static final String CHANNEL_NAME = "Example Channel";
  private static final int NOTIFICATION_ID = 1;

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "[Push Notification] Push notification has been received in broadcast receiver");
    showNotification(context);
  }

  public static void showNotification(Context context) {
    NotificationManager notificationManager =
      (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    NotificationChannel channel = new NotificationChannel(
      CHANNEL_ID,
      CHANNEL_NAME,
      NotificationManager.IMPORTANCE_DEFAULT
    );
    notificationManager.createNotificationChannel(channel);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle("Simple Notification")
      .setContentText("This is a simple notification.")
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true);

    notificationManager.notify(NOTIFICATION_ID, builder.build());
  }
}
