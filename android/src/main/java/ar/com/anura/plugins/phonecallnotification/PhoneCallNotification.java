package ar.com.anura.plugins.phonecallnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class PhoneCallNotification {

  private static AppCompatActivity activity;
  static NotificationSettings incomingCallNotificationSettings;
  static NotificationSettings callInProgressNotificationSettings;
  static IncomingCallNotificationListener incomingCallNotificationListener;
  static CallInProgressNotificationListener callInProgressNotificationListener;

  public static String INCOMING_CALL_TAP_ACTION = "tap_incoming_call_notification";
  public static String INCOMING_CALL_DECLINE_ACTION = "decline_incoming_call";
  public static String INCOMING_CALL_ANSWER_ACTION = "answer_incoming_call";
  public static String INCOMING_CALL_TERMINATE_ACTION = "terminate_current_call";

  public static String CALL_IN_PROGRESS_TAP_ACTION = "tap_call_in_progress_notification";
  public static String CALL_IN_PROGRESS_HOLD_ACTION = "hold_call_in_progress";
  public static String CALL_IN_PROGRESS_TERMINATE_ACTION = "terminate_call_in_progress";

  public static final int INCOMING_CALL_NOTIFICATION_ID = -574543923; // Random ID
  public static final int CALL_IN_PROGRESS_NOTIFICATION_ID = -234414143; // Random ID

  public static void initialize(final AppCompatActivity activity) {
    PhoneCallNotification.activity = activity;
    incomingCallNotificationSettings = new NotificationSettings();
    callInProgressNotificationSettings = new NotificationSettings();
  }

  public static void showIncomingCallNotification(final NotificationSettings settings, final IncomingCallNotificationListener listener) {
    incomingCallNotificationSettings = settings;
    incomingCallNotificationListener = listener;

    Context context = activity.getApplicationContext();
    String iconName = incomingCallNotificationSettings.getIcon();
    int iconResource = getIconResId(iconName);
    if (iconResource == 0) { // If no icon at all was found, fall back to the app's icon
      iconResource = context.getApplicationInfo().icon;
    }

    String pictureName = incomingCallNotificationSettings.getPicture();
    int pictureResource = getIconResId(pictureName);
    if (pictureResource == 0) { // If no icon at all was found, fall back to the app's icon
      pictureResource = context.getApplicationInfo().icon;
    }

    final String CHANNEL_ID = "incoming-call-notification-channel-id";
    final NotificationChannel notificationChannel = getNotificationChannel(incomingCallNotificationSettings, CHANNEL_ID);
    notificationChannel.setSound(null, null);
    // Register the channel with the system; you can't change the importance or other notification behaviors after this
    getNotificationManager().createNotificationChannel(notificationChannel);

    Notification.Builder notificationBuilder = new Notification.Builder(context, CHANNEL_ID)
      .setContentTitle(incomingCallNotificationSettings.getChannelName())
      // Ongoing notifications cannot be dismissed by the user
      .setOngoing(true)
      // Set the "ticker" text which is sent to accessibility services.
      .setTicker(incomingCallNotificationSettings.getChannelName())
      // To know if it is necessary to disturb the user with a notification despite having activated the "Do not interrupt" mode
      .setCategory(Notification.CATEGORY_CALL)
      // Add a timestamp pertaining to the notification
      .setWhen(System.currentTimeMillis())
      // VISIBILITY_PUBLIC displays the full content of the notification
      .setVisibility(Notification.VISIBILITY_PUBLIC)
      // Make this notification automatically dismissed when the user touches it.
      .setAutoCancel(false)
      .setContentIntent(getPendingIntent(INCOMING_CALL_TAP_ACTION))
      .setColor(Color.parseColor(incomingCallNotificationSettings.getColor()))
      // Set whether or not this notification should not bridge to other devices.
      .setLocalOnly(true);

    Boolean thereIsACallInProgress = incomingCallNotificationSettings.getThereIsACallInProgress();

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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !thereIsACallInProgress) {
      Icon icon = Icon.createWithResource(context, pictureResource);
      Person caller = new Person.Builder()
        .setIcon(icon)
        .setName(incomingCallNotificationSettings.getCallerName() + " - " + incomingCallNotificationSettings.getCallerNumber())
        .setImportant(true)
        .build();

      Notification.CallStyle notificationStyle;
      notificationStyle =
        Notification.CallStyle.forIncomingCall(caller, getPendingIntent(INCOMING_CALL_DECLINE_ACTION), getPendingIntent(INCOMING_CALL_ANSWER_ACTION));

      notificationStyle.setAnswerButtonColorHint(Color.parseColor(incomingCallNotificationSettings.getAnswerButtonColor()));
      notificationStyle.setDeclineButtonColorHint(Color.parseColor(incomingCallNotificationSettings.getDeclineButtonColor()));
      notificationBuilder.setStyle((notificationStyle));
      notificationBuilder.setSmallIcon(getIconResId("answer", "drawable"));
      notificationBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
    } else {
      notificationBuilder.setSmallIcon(iconResource);
      notificationBuilder.setContentText(incomingCallNotificationSettings.getCallerName() + " - " + incomingCallNotificationSettings.getCallerNumber());

      if (thereIsACallInProgress) {
        Notification.Action answerAction = new Notification.Action.Builder(
          Icon.createWithResource(context, getIconResId("hold_answer", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(incomingCallNotificationSettings.getHoldAndAnswerButtonColor()) +
              "\">" +
              incomingCallNotificationSettings.getHoldAndAnswerButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_ANSWER_ACTION)
        )
          .build();

        Notification.Action declineAction = new Notification.Action.Builder(
          Icon.createWithResource(context, getIconResId("decline", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(incomingCallNotificationSettings.getDeclineSecondCallButtonColor()) +
              "\">" +
              incomingCallNotificationSettings.getDeclineSecondCallButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_DECLINE_ACTION)
        )
          .build();

        Notification.Action terminateAction = new Notification.Action.Builder(
          Icon.createWithResource(context, getIconResId("terminate_answer", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(incomingCallNotificationSettings.getTerminateAndAnswerButtonColor()) +
              "\">" +
              incomingCallNotificationSettings.getTerminateAndAnswerButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_TERMINATE_ACTION)
        )
          .build();
        notificationBuilder.setActions(terminateAction, declineAction, answerAction);
      } else {
        Notification.Action answerAction = new Notification.Action.Builder(
          Icon.createWithResource(context, getIconResId("answer", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(incomingCallNotificationSettings.getAnswerButtonColor()) +
              "\">" +
              incomingCallNotificationSettings.getAnswerButtonText() +
              "</font>",
            Html.FROM_HTML_MODE_LEGACY
          ),
          getPendingIntent(INCOMING_CALL_ANSWER_ACTION)
        )
          .build();

        Notification.Action declineAction = new Notification.Action.Builder(
          Icon.createWithResource(context, getIconResId("decline", "drawable")),
          Html.fromHtml(
            "<font color=\"" +
              Color.parseColor(incomingCallNotificationSettings.getDeclineButtonColor()) +
              "\">" +
              incomingCallNotificationSettings.getDeclineButtonText() +
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
    getNotificationManager().notify(INCOMING_CALL_NOTIFICATION_ID, notification);
  }

  public static void showCallInProgressNotification(final NotificationSettings settings, final CallInProgressNotificationListener listener) {
    callInProgressNotificationSettings = settings;
    callInProgressNotificationListener = listener;

    Context context = activity.getApplicationContext();

    Intent intent = new Intent(context, CallInProgressNotificationService.class);
    context.startForegroundService(intent);
  }

  public static void hideIncomingCall() {
    incomingCallNotificationSettings = null;
    getNotificationManager().cancel(INCOMING_CALL_NOTIFICATION_ID);
  }

  public static void hideCallInProgress() {
    callInProgressNotificationSettings = null;
    Context context = activity.getApplicationContext();
    Intent intent = new Intent(context, CallInProgressNotificationService.class);
    context.stopService(intent);
  }

  public static boolean areNotificationsEnabled() {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
    return notificationManager.areNotificationsEnabled();
  }

  private static PendingIntent getPendingIntent(String action) {
    Context context = activity.getApplicationContext();
    Intent intent = new Intent(context, IncomingCallNotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.setAction(action);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
  }

  /**
   * Returns the shared notification service manager.
   */
  private static NotificationManager getNotificationManager() {
    return (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @NonNull
  private static NotificationChannel getNotificationChannel(NotificationSettings settings, String CHANNEL_ID) {
    final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    final long[] DEFAULT_VIBRATE_PATTERN = { 0, 250, 250, 250 };

    final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, settings.getChannelName(), CHANNEL_IMPORTANCE);
    notificationChannel.setDescription(settings.getChannelDescription());
    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
    notificationChannel.enableVibration(true);
    notificationChannel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
    return notificationChannel;
  }

  /**
   * Retrieves the resource ID of the sent icon name
   *
   * @param name Name of the resource to return
   */
  private static int getIconResId(String name) {
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
  private static int getIconResId(String icon, String type) {
    Resources res = activity.getResources();
    String pkgName = activity.getPackageName();

    return res.getIdentifier(icon, type, pkgName);
  }
}
