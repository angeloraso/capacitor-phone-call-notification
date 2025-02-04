package ar.com.anura.plugins.phonecallnotification;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = "PushNotificationService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String message = remoteMessageToString(remoteMessage);
        Log.d(TAG, "FCM remote message: " + message);

      if (PhoneCallNotificationPlugin.isAppInForeground()) {
        PhoneCallNotificationPlugin.onRemoteMessage(remoteMessage);
      } else {
        NotificationSettings settings = PhoneCallNotification.getPushNotificationSettings();
        settings.setCallerName("Push notification");
        settings.setCallerNumber("2213456789");

        if (!PhoneCallNotificationPlugin.isAppInForeground()) {
          PhoneCallNotification.showIncomingCallNotification(
            PhoneCallNotification.getPushNotificationSettings(),
            new IncomingCallNotificationListener() {
              @Override
              public void onTap() {
                Log.d(TAG, "Push notification: TAP");
              }

              @Override
              public void onDecline() {
                Log.d(TAG, "Push notification: DECLINE");
              }

              @Override
              public void onAnswer() {
                Log.d(TAG, "Push notification: ANSWER");
              }

              @Override
              public void onTerminate() {
                Log.d(TAG, "Push notification: TERMINATE");
              }
            });
        }
      }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM token: " + token);
        PhoneCallNotificationPlugin.onNewToken(token);
    }


  private String remoteMessageToString(RemoteMessage remoteMessage) {
    StringBuilder builder = new StringBuilder();
    builder.append("From [");
    builder.append(remoteMessage.getFrom());
    builder.append("], Message Id [");
    builder.append(remoteMessage.getMessageId());
    builder.append("], TTL [");
    builder.append(remoteMessage.getTtl());
    builder.append("], Original Priority [");
    builder.append(remoteMessage.getOriginalPriority());
    builder.append("], Received Priority [");
    builder.append(remoteMessage.getPriority());
    builder.append("], Sent Time [");
    builder.append(remoteMessage.getSentTime());
    builder.append("], Data [");
    builder.append(new JSONObject(remoteMessage.getData()).toString());
    builder.append("]");
    return builder.toString();
  }
}
