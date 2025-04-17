package ar.com.anura.plugins.phonecallnotification;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_ANSWER_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_DECLINE_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_NOTIFICATION_ID;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_TERMINATE_ACTION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;

public class IncomingCallNotificationService extends Service {
  private static boolean shouldStop = false;
  private static final String TAG = "IncomingCallNotificationService";

  public IncomingCallNotificationService() {}

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    stopForeground(true);
    getNotificationManager().cancel(INCOMING_CALL_NOTIFICATION_ID);
  }

  /**
   * START_NOT_STICKY: if the process (the App) is killed with no remaining start commands to deliver,
   * then the service will be stopped instead of restarted
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    IncomingPhoneCallNotificationSettings settings = (IncomingPhoneCallNotificationSettings) intent.getSerializableExtra("settings");

    if (settings == null) {
      settings = new IncomingPhoneCallNotificationSettings.Builder().build();
    }

    createNotification(settings);
    return START_NOT_STICKY;
  }

  public void createNotification(IncomingPhoneCallNotificationSettings settings) {
    String iconName = settings.getIcon();
    int iconResource = getIconResId(iconName);
    if (iconResource == 0) { // If no icon at all was found, fall back to the app's icon
      iconResource = getApplicationContext().getApplicationInfo().icon;
    }

    String pictureName = settings.getPicture();
    int pictureResource = getIconResId(pictureName);
    if (pictureResource == 0) { // If no icon at all was found, fall back to the app's icon
      pictureResource = getApplicationContext().getApplicationInfo().icon;
    }

    final String CHANNEL_ID = "incoming-call-notification-channel-id";
    final NotificationChannel notificationChannel = getNotificationChannel(settings, CHANNEL_ID);
    // Register the channel with the system; you can't change the importance or other notification behaviors after this
    getNotificationManager().createNotificationChannel(notificationChannel);

    Notification.Builder notificationBuilder = new Notification.Builder(this, CHANNEL_ID)
      .setContentTitle(settings.getChannelName())
      // Ongoing notifications cannot be dismissed by the user
      .setOngoing(true)
      // Set the "ticker" text which is sent to accessibility services.
      .setTicker(settings.getChannelName())
      // To know if it is necessary to disturb the user with a notification despite having activated the "Do not interrupt" mode
      .setCategory(Notification.CATEGORY_CALL)
      // Add a timestamp pertaining to the notification
      .setWhen(System.currentTimeMillis())
      // VISIBILITY_PUBLIC displays the full content of the notification
      .setVisibility(Notification.VISIBILITY_PUBLIC)
      // Make this notification automatically dismissed when the user touches it.
      .setAutoCancel(false)
      .setContentIntent(getPendingIntent(INCOMING_CALL_TAP_ACTION))
      .setColor(Color.parseColor(settings.getColor()))
      // Set whether or not this notification should not bridge to other devices.
      .setLocalOnly(true);

    Boolean callWaiting = settings.getCallWaiting();

    Intent intent = new Intent(getApplicationContext(), IncomingCallFullNotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(
      getApplicationContext(),
      0,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );
    notificationBuilder.setFullScreenIntent(pendingIntent, true);

    // Android 12+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !callWaiting) {
      Icon icon = Icon.createWithResource(this, pictureResource);
      Person caller = new Person.Builder()
        .setIcon(icon)
        .setName(settings.getCallingName() + " - " + settings.getCallingNumber())
        .setImportant(true)
        .build();

      Notification.CallStyle notificationStyle;
      notificationStyle = Notification.CallStyle.forIncomingCall(caller, getPendingIntent(INCOMING_CALL_DECLINE_ACTION), getPendingIntent(INCOMING_CALL_ANSWER_ACTION));

      notificationStyle.setAnswerButtonColorHint(Color.parseColor(settings.getAnswerButtonColor()));
      notificationStyle.setDeclineButtonColorHint(Color.parseColor(settings.getDeclineButtonColor()));
      notificationBuilder.setStyle((notificationStyle));
      notificationBuilder.setSmallIcon(getIconResId("answer", "drawable"));
      notificationBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
    } else {
      notificationBuilder.setSmallIcon(iconResource);
      notificationBuilder.setContentText(settings.getCallingName() + " - " + settings.getCallingNumber());

      if (callWaiting) {
        Notification.Action answerAction = new Notification.Action.Builder(
          Icon.createWithResource(this, getIconResId("hold_answer", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(settings.getHoldAndAnswerButtonColor()) +
              "\">" +
              settings.getHoldAndAnswerButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_ANSWER_ACTION)
        )
          .build();

        Notification.Action declineAction = new Notification.Action.Builder(
          Icon.createWithResource(this, getIconResId("decline", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(settings.getDeclineCallWaitingButtonColor()) +
              "\">" +
              settings.getDeclineCallWaitingButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_DECLINE_ACTION)
        )
          .build();

        Notification.Action terminateAction = new Notification.Action.Builder(
          Icon.createWithResource(this, getIconResId("terminate_answer", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(settings.getTerminateAndAnswerButtonColor()) +
              "\">" +
              settings.getTerminateAndAnswerButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_TERMINATE_ACTION)
        )
          .build();
        notificationBuilder.setActions(terminateAction, declineAction, answerAction);
      } else {
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
          getPendingIntent(INCOMING_CALL_ANSWER_ACTION)
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
          getPendingIntent(INCOMING_CALL_DECLINE_ACTION)
        )
          .build();

        notificationBuilder.setActions(declineAction, answerAction);
      }
    }

    Notification notification = notificationBuilder.build();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      startForeground(INCOMING_CALL_NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SHORT_SERVICE);
    } else {
      startForeground(INCOMING_CALL_NOTIFICATION_ID, notification);
    }

    if (shouldStop) {
      Log.d(TAG, "Should stop service");
      stopSelf();
    }
  }

  private PendingIntent getPendingIntent(String action) {
    Context context = getApplicationContext();
    Intent intent = new Intent(context, IncomingCallNotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.setAction(action);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
  }

  @NonNull
  private static NotificationChannel getNotificationChannel(IncomingPhoneCallNotificationSettings settings, String CHANNEL_ID) {
    final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    final long[] DEFAULT_VIBRATE_PATTERN = { 0, 250, 250, 250 };

    final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, settings.getChannelName(), CHANNEL_IMPORTANCE);
    notificationChannel.setDescription(settings.getChannelDescription());
    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
    notificationChannel.enableVibration(true);
    notificationChannel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
    notificationChannel.setSound(null, null);
    return notificationChannel;
  }

  /**
   * Returns the shared notification service manager.
   */
  private NotificationManager getNotificationManager() {
    return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
  }

  /**
   * Retrieves the resource ID of the sent icon name
   *
   * @param name Name of the resource to return
   */
  private int getIconResId(String name) {
    int resId = getIconResId(name, "mipmap");

    if (resId == 0) {
      resId = getIconResId(name, "drawable");
    }

    if (resId == 0) {
      resId = getIconResId("icon", "mipmap");
    }

    if (resId == 0) {
      resId = getIconResId("icon", "drawable");
    }

    return resId;
  }

  /**
   * Retrieve resource id of the specified icon.
   *
   * @param icon The name of the icon.
   * @param type The resource type where to look for.
   *
   * @return The resource id or 0 if not found.
   */
  private int getIconResId(String icon, String type) {
    Resources res = getResources();
    String pkgName = getPackageName();

    return res.getIdentifier(icon, type, pkgName);
  }

  public static void requestStop() {
    shouldStop = true;
  }

  public static void requestStart() {
    shouldStop = false;
  }
}
