package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.core.app.NotificationManagerCompat;
import java.lang.ref.WeakReference;

public class PhoneCallNotification {

  private static final String TAG = "PhoneCallNotification";
  private static final String PREFERENCES_NAME = "phone-call-notification";
  private static final String PENDING_RESPONSE_KEY = "pending-response";
  private static Context applicationContext;
  private static WeakReference<ResponseListener> responseListener = new WeakReference<>(null);

  public static final String INCOMING_CALL_TAP_ACTION = "tap_incoming_call_notification";
  public static final String INCOMING_CALL_DECLINE_ACTION = "decline_incoming_call";
  public static final String INCOMING_CALL_ANSWER_ACTION = "answer_incoming_call";
  public static final String INCOMING_CALL_TERMINATE_ACTION = "terminate_current_call";

  public static final String CALL_IN_PROGRESS_TAP_ACTION = "tap_call_in_progress_notification";
  public static final String CALL_IN_PROGRESS_HOLD_ACTION = "hold_call_in_progress";
  public static final String CALL_IN_PROGRESS_TERMINATE_ACTION = "terminate_call_in_progress";

  public static final int INCOMING_CALL_NOTIFICATION_ID = -574543923; // Random ID
  public static final int CALL_IN_PROGRESS_NOTIFICATION_ID = -234414143; // Random ID

  interface ResponseListener {
    void onResponse(String response);
  }

  public static void initialize(Context context, ResponseListener listener) {
    applicationContext = context.getApplicationContext();
    responseListener = new WeakReference<>(listener);
  }

  public static void clearResponseListener(ResponseListener listener) {
    if (responseListener.get() == listener) {
      responseListener.clear();
    }
  }

  public static String consumePendingResponse() {
    SharedPreferences preferences = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    String response = preferences.getString(PENDING_RESPONSE_KEY, null);
    preferences.edit().remove(PENDING_RESPONSE_KEY).apply();
    return response;
  }

  public static void dispatchResponse(Context context, String response) {
    ResponseListener listener = responseListener.get();
    if (listener != null) {
      listener.onResponse(response);
      return;
    }

    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putString(PENDING_RESPONSE_KEY, response).apply();
  }

  public static void showIncomingCallNotification(final IncomingPhoneCallNotificationSettings settings) {
    if (!areNotificationsEnabled()) {
      throw new IllegalStateException("Notifications are disabled");
    }

    Log.d(TAG, "showIncomingCallNotification");
    IncomingCallNotificationService.startService(getContext(), settings);
  }

  public static void showCallInProgressNotification(final CallInProgressNotificationSettings settings) {
    if (!areNotificationsEnabled()) {
      throw new IllegalStateException("Notifications are disabled");
    }

    Log.d(TAG, "showCallInProgressNotification");
    CallInProgressNotificationService.startService(getContext(), settings);
  }

  public static void hideIncomingPhoneCallNotification() {
    Log.d(TAG, "hideIncomingPhoneCallNotification");
    IncomingCallNotificationService.stopService(getContext());
  }

  public static void hideCallInProgressNotification() {
    Log.d(TAG, "hideCallInProgressNotification");
    CallInProgressNotificationService.stopService(getContext());
  }

  public static boolean areNotificationsEnabled() {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
    return notificationManager.areNotificationsEnabled();
  }

  private static Context getContext() {
    if (applicationContext == null) {
      throw new IllegalStateException("PhoneCallNotification is not initialized");
    }
    return applicationContext;
  }
}
