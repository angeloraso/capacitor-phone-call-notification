package ar.com.anura.plugins.phonecallnotification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.PowerManager;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

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
      Runnable runnable;
      if (PhoneCallNotificationPlugin.isAppInForeground()) {
        runnable = () -> PhoneCallNotificationPlugin.onMessageReceived(remoteMessage);
      } else {
        runnable = () -> {
          PowerManager mPowerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
          PowerManager.WakeLock wakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG + ":Push Notification Processing");
          wakeLock.acquire(20000L); // 20 seconds

          storePushRemoteMessage(remoteMessage);

          Intent intent = new Intent();
          String action = "ar.com.anura.plugins.phonecallnotification.action.PUSH_NOTIFICATION_RECEIVED";
          intent.setAction(action);

          String appPackageName = getPackageName();
          boolean found = false;
          android.util.Log.i(TAG, "[Push Notification] Looking for an application with package name [" + appPackageName + "] that has registered a BroadcastReceiver for an Intent with action [" + action + "]");

          PackageManager pm = getPackageManager();
          List<ResolveInfo> matches = pm.queryBroadcastReceivers(intent, 0);
          for (ResolveInfo resolveInfo : matches) {
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (packageName.equals(appPackageName)) {
              android.util.Log.i(TAG, "[Push Notification] Notifying component [" + resolveInfo.activityInfo.name + "] using a Broadcast Intent with action [" + action + "]");
              Intent explicit = new Intent(intent);
              ComponentName cn = new ComponentName(packageName, resolveInfo.activityInfo.name);
              explicit.setComponent(cn);
              sendBroadcast(explicit);
              found = true;
              break;
            }
          }

          if (!found) {
            Log.e(TAG, "[Push Notification] No matching BroadcastReceiver found for an Intent with action [" + action + "]!");
          }

          wakeLock.release();
        };
      }
      AndroidDispatcher.dispatchOnUIThread(runnable);
    }

    private void storePushRemoteMessage(RemoteMessage remoteMessage) {
      Context context = getApplicationContext();
      SharedPreferences sharedPref = context.getSharedPreferences("push_notification_storage", Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();

      Map<String, String> data = remoteMessage.getData();
      String callId = getCallId(data);
      editor.putString("call-id", callId);
      String payload = new JSONObject(data).toString();
      editor.putString("payload", payload);
      editor.apply();
      android.util.Log.d(TAG, "[Push Notification] Push information stored for Call-ID [" + callId + "]");
    }

    private String getCallId(Map<String, String> map) {
      if (map.containsKey("call-id")) {
        String result = map.get("call-id");
        if (result != null) {
          return result;
        }
      }
      return "";
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
