package ar.com.anura.plugins.phonecallnotification;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = "PushNotificationService";

    @Override
    public void onNewToken(@NonNull final String token) {
      super.onNewToken(token);
      Log.d(TAG, "[Push Notification] Refreshed token: " + token);
      if (PhoneCallNotificationPlugin.isAppInForeground()) {
        Runnable runnable = () -> PhoneCallNotificationPlugin.onNewToken(token);
        AndroidDispatcher.dispatchOnUIThread(runnable);
      }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "[Push Notification] Remote message received: " + remoteMessageToString(remoteMessage));
        if (PhoneCallNotificationPlugin.isAppInForeground()) {
          Runnable runnable = () -> PhoneCallNotificationPlugin.onMessageReceived(remoteMessage);
          AndroidDispatcher.dispatchOnUIThread(runnable);
        } else {
          Runnable runnable = () -> {
            
          };
          AndroidDispatcher.dispatchOnUIThread(runnable);
      }
    }

    private Class<? extends AppCompatActivity> getMainActivityClass(Context context) {
      try {
        String packageName = context.getPackageName();
        Class<?> mainActivityClass = Class.forName(packageName + ".MainActivity");
        if (AppCompatActivity.class.isAssignableFrom(mainActivityClass)) {
          return mainActivityClass.asSubclass(AppCompatActivity.class);
        } else {
          throw new RuntimeException("MainActivity does not extend AppCompatActivity.");
        }
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Unable to resolve MainActivity class.");
      }
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
