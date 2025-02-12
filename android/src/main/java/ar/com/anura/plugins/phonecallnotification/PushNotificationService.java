package ar.com.anura.plugins.phonecallnotification;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

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
      Runnable runnable;
      if (PhoneCallNotificationPlugin.isAppInForeground()) {
        runnable = () -> PhoneCallNotificationPlugin.onMessageReceived(remoteMessage);
      } else {
        runnable = () -> {
          PowerManager mPowerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
          PowerManager.WakeLock wakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG + ":Push Notification Processing");
          wakeLock.acquire(20000L); // 20 seconds

          showIncomingCallNotification(getApplicationContext(), remoteMessage);

          wakeLock.release();
        };
      }
      AndroidDispatcher.dispatchOnUIThread(runnable);
    }

  private void showIncomingCallNotification(Context context, RemoteMessage remoteMessage) {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

    NotificationSettings settings = new NotificationSettings();

    final String CHANNEL_ID = "incoming-call-notification-channel-id";
    final NotificationChannel notificationChannel = getNotificationChannel(CHANNEL_ID, settings);
    notificationChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null);
    notificationManager.createNotificationChannel(notificationChannel);

    String TAP_ACTION = "tap_incoming_call_notification";
    String DECLINE_ACTION = "decline_incoming_call";
    String ANSWER_ACTION = "answer_incoming_call";

    Notification.Builder notificationBuilder = new Notification.Builder(this, CHANNEL_ID)
      .setContentTitle(settings.getChannelName())
      .setOngoing(true)
      .setTicker(settings.getChannelName())
      .setCategory(Notification.CATEGORY_CALL)
      .setWhen(System.currentTimeMillis())
      .setVisibility(Notification.VISIBILITY_PUBLIC)
      .setAutoCancel(false)
      .setContentIntent(getPendingIntent(context,TAP_ACTION))
      .setColor(Color.parseColor(settings.getColor()))
      .setLocalOnly(true);

    Intent intent = new Intent(context, IncomingCallFullNotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(
      context,
      0,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );
    notificationBuilder.setFullScreenIntent(pendingIntent, true);

    // Android 12+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Icon icon = Icon.createWithResource(this, getApplicationContext().getApplicationInfo().icon);
      Person caller = new Person.Builder()
        .setIcon(icon)
        .setName(remoteMessage.getData().get("from_display_name"))
        .setImportant(true)
        .build();

      Notification.CallStyle notificationStyle;
      notificationStyle =
        Notification.CallStyle.forIncomingCall(caller, getPendingIntent(context, DECLINE_ACTION), getPendingIntent(context, ANSWER_ACTION));

      notificationStyle.setAnswerButtonColorHint(Color.parseColor(settings.getAnswerButtonColor()));
      notificationStyle.setDeclineButtonColorHint(Color.parseColor(settings.getDeclineButtonColor()));
      notificationBuilder.setStyle((notificationStyle));
      notificationBuilder.setSmallIcon(getIconResId("answer", "drawable"));
      notificationBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
    } else {
      notificationBuilder.setSmallIcon(getApplicationContext().getApplicationInfo().icon);
      notificationBuilder.setContentText(remoteMessage.getData().get("from_display_name"));

      Notification.Action answerAction = new Notification.Action.Builder(
        Icon.createWithResource(this, getIconResId("answer", "drawable")),
        Html.fromHtml(
          "<font color=\"" +
            Color.parseColor(settings.getAnswerButtonColor()) +
            "\">" +
            settings.getAnswerButtonText() +
            "</font>",
          Html.FROM_HTML_MODE_LEGACY
        ),
        getPendingIntent(context, ANSWER_ACTION)
      )
        .build();

      Notification.Action declineAction = new Notification.Action.Builder(
        Icon.createWithResource(this, getIconResId("decline", "drawable")),
        Html.fromHtml(
          "<font color=\"" +
            Color.parseColor(settings.getDeclineButtonColor()) +
            "\">" +
            settings.getDeclineButtonText() +
            "</font>",
          Html.FROM_HTML_MODE_LEGACY
        ),
        getPendingIntent(context, DECLINE_ACTION)
      )
        .build();

      notificationBuilder.setActions(declineAction, answerAction);
    }

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
      return;
    }

    Notification notification = notificationBuilder.build();
    notificationManager.notify(1001, notification);
  }

  @NonNull
  private static NotificationChannel getNotificationChannel(String CHANNEL_ID, NotificationSettings settings) {
    final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    final long[] DEFAULT_VIBRATE_PATTERN = { 0, 250, 250, 250 };

    final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, settings.getChannelName(), CHANNEL_IMPORTANCE);
    notificationChannel.setDescription(settings.getChannelDescription());
    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
    notificationChannel.enableVibration(true);
    notificationChannel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
    return notificationChannel;
  }

  private PendingIntent getPendingIntent(Context context, String action) {
    Intent intent = new Intent(context, IncomingCallNotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.setAction(action);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
  }

  private int getIconResId(String icon, String type) {
    Resources res = getResources();
    String pkgName = getPackageName();

    return res.getIdentifier(icon, type, pkgName);
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
