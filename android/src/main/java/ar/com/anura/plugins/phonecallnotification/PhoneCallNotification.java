package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class PhoneCallNotification {
  private static final String TAG = "PhoneCallNotification";
  private static AppCompatActivity activity;
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
  }

  public static void showIncomingCallNotification(final IncomingPhoneCallNotificationSettings settings, final IncomingCallNotificationListener listener) {
    if (!areNotificationsEnabled()) {
      return;
    }

    Log.d(TAG, "showIncomingCallNotification");
    incomingCallNotificationListener = listener;
    IncomingCallNotificationService.startService(activity.getApplicationContext(), settings);
  }

  public static void showCallInProgressNotification(final CallInProgressNotificationSettings settings, final CallInProgressNotificationListener listener) {
    if (!areNotificationsEnabled()) {
      return;
    }

    Log.d(TAG, "showCallInProgressNotification");
    callInProgressNotificationListener = listener;
    CallInProgressNotificationService.startService(activity.getApplicationContext(), settings);
  }

  public static void hideIncomingPhoneCallNotification() {
    Log.d(TAG, "hideIncomingPhoneCallNotification");
    IncomingCallNotificationService.stopService(activity.getApplicationContext());
  }

  public static void hideCallInProgressNotification() {
    Log.d(TAG, "hideCallInProgressNotification");
    CallInProgressNotificationService.stopService(activity.getApplicationContext());
  }

  public static boolean areNotificationsEnabled() {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
    return notificationManager.areNotificationsEnabled();
  }
}
