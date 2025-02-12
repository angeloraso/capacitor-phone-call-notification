package ar.com.anura.plugins.phonecallnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class PushNotificationReceiver extends BroadcastReceiver {
  private static final String TAG = "PushNotificationReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "[Push Notification] Push notification has been received in broadcast receiver");

    String action = intent.getAction();

    if ("ACTION_ACCEPT_CALL".equals(action)) {
      Log.d("PushNotificationReceiver", "Llamada aceptada");
    } else if ("ACTION_REJECT_CALL".equals(action)) {
      Log.d("PushNotificationReceiver", "Llamada rechazada");
    }
  }
}
