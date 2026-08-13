package ar.com.anura.plugins.phonecallnotification;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
  name = "PhoneCallNotification",
  permissions = @Permission(
    strings = { Manifest.permission.POST_NOTIFICATIONS },
    alias = PhoneCallNotificationPlugin.PHONE_CALL_NOTIFICATIONS_PERMISSION
  )
)
public class PhoneCallNotificationPlugin extends Plugin implements PhoneCallNotification.ResponseListener {

  static final String PHONE_CALL_NOTIFICATIONS_PERMISSION = "notifications";

  public void load() {
    PhoneCallNotification.initialize(getContext(), this);
    String pendingResponse = PhoneCallNotification.consumePendingResponse();
    if (pendingResponse != null) {
      onPhoneCallNotificationEvent(pendingResponse, true);
    }
  }

  @Override
  protected void handleOnDestroy() {
    PhoneCallNotification.clearResponseListener(this);
    super.handleOnDestroy();
  }

  @Override
  public void onResponse(String response) {
    onPhoneCallNotificationEvent(response);
  }

  private void onPhoneCallNotificationEvent(String response) {
    onPhoneCallNotificationEvent(response, true);
  }

  private void onPhoneCallNotificationEvent(String response, boolean retainUntilConsumed) {
    JSObject res = new JSObject();
    res.put("response", response);
    notifyListeners("response", res, retainUntilConsumed);
  }

  @PluginMethod
  public void showIncomingPhoneCallNotification(PluginCall call) {
    if (getActivity() == null || getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

    try {
      PhoneCallNotification.showIncomingCallNotification(getIncomingPhoneCallNotificationSettings(call));
      call.resolve();
    } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
      call.reject("Unable to show incoming call notification: " + e.getMessage(), e);
    } catch (RuntimeException e) {
      call.reject("Android did not allow the incoming call foreground service to start", e);
    }
  }

  @PluginMethod
  public void showCallInProgressNotification(PluginCall call) {
    if (getActivity() == null || getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

    try {
      PhoneCallNotification.showCallInProgressNotification(getCallInProgressNotificationSettings(call));
      call.resolve();
    } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
      call.reject("Unable to show call in progress notification: " + e.getMessage(), e);
    } catch (RuntimeException e) {
      call.reject("Android did not allow the call foreground service to start", e);
    }
  }

  @PluginMethod
  public void hideIncomingPhoneCallNotification(PluginCall call) {
    PhoneCallNotification.hideIncomingPhoneCallNotification();
    call.resolve();
  }

  @PluginMethod
  public void hideCallInProgressNotification(PluginCall call) {
    PhoneCallNotification.hideCallInProgressNotification();
    call.resolve();
  }

  @PluginMethod
  public void hideAll(PluginCall call) {
    PhoneCallNotification.hideIncomingPhoneCallNotification();
    PhoneCallNotification.hideCallInProgressNotification();
    call.resolve();
  }

  @PluginMethod
  public void checkNotificationsPermission(PluginCall call) {
    notificationPermissionCallback(call);
  }

  @PluginMethod
  public void requestNotificationsPermission(PluginCall call) {
    if (
      Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
      getPermissionState(PHONE_CALL_NOTIFICATIONS_PERMISSION) == PermissionState.GRANTED
    ) {
      notificationPermissionCallback(call);
    } else {
      requestPermissionForAlias(PHONE_CALL_NOTIFICATIONS_PERMISSION, call, "notificationPermissionCallback");
    }
  }

  @PluginMethod
  public void checkFullScreenIntentPermission(PluginCall call) {
    JSObject result = new JSObject();
    result.put("fullScreenIntent", getPermissionText(canUseFullScreenIntent()));
    call.resolve(result);
  }

  @PluginMethod
  public void openFullScreenIntentSettings(PluginCall call) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE || canUseFullScreenIntent()) {
      call.resolve();
      return;
    }

    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT, Uri.parse("package:" + getContext().getPackageName()));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
      getContext().startActivity(intent);
      call.resolve();
    } catch (RuntimeException exception) {
      call.reject("Unable to open the full-screen intent settings", exception);
    }
  }

  @PermissionCallback
  private void notificationPermissionCallback(PluginCall call) {
    JSObject permissionsResultJSON = new JSObject();
    permissionsResultJSON.put(PHONE_CALL_NOTIFICATIONS_PERMISSION, getPermissionText(PhoneCallNotification.areNotificationsEnabled()));
    call.resolve(permissionsResultJSON);
  }

  private String getPermissionText(boolean enabled) {
    if (enabled) {
      return "granted";
    } else {
      return "denied";
    }
  }

  private boolean canUseFullScreenIntent() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      return true;
    }
    NotificationManager manager = getContext().getSystemService(NotificationManager.class);
    return manager != null && manager.canUseFullScreenIntent();
  }

  private IncomingPhoneCallNotificationSettings getIncomingPhoneCallNotificationSettings(PluginCall call) {
    String icon = call.getString("icon");
    String picture = call.getString("picture");
    Boolean callWaiting = call.getBoolean("callWaiting");
    String declineButtonText = call.getString("declineButtonText");
    String declineButtonColor = call.getString("declineButtonColor");
    String answerButtonText = call.getString("answerButtonText");
    String answerButtonColor = call.getString("answerButtonColor");
    String terminateAndAnswerButtonText = call.getString("terminateAndAnswerButtonText");
    String terminateAndAnswerButtonColor = call.getString("terminateAndAnswerButtonColor");
    String terminateButtonText = call.getString("terminateButtonText");
    String terminateButtonColor = call.getString("terminateButtonColor");
    String declineCallWaitingButtonText = call.getString("declineCallWaitingButtonText");
    String declineCallWaitingButtonColor = call.getString("declineCallWaitingButtonColor");
    String holdButtonText = call.getString("holdButtonText");
    String holdButtonColor = call.getString("holdButtonColor");
    String holdAndAnswerButtonText = call.getString("holdAndAnswerButtonText");
    String holdAndAnswerButtonColor = call.getString("holdAndAnswerButtonColor");
    String color = call.getString("color");
    Integer durationValue = call.getInt("duration");
    int duration = durationValue != null ? durationValue : 0;
    String channelName = call.getString("channelName");
    String channelDescription = call.getString("channelDescription");
    String callingName = call.getString("callingName");
    String callingNumber = call.getString("callingNumber");

    validateDuration(duration);
    validateColor("color", color);
    validateColor("declineButtonColor", declineButtonColor);
    validateColor("answerButtonColor", answerButtonColor);
    validateColor("terminateAndAnswerButtonColor", terminateAndAnswerButtonColor);
    validateColor("terminateButtonColor", terminateButtonColor);
    validateColor("declineCallWaitingButtonColor", declineCallWaitingButtonColor);
    validateColor("holdButtonColor", holdButtonColor);
    validateColor("holdAndAnswerButtonColor", holdAndAnswerButtonColor);

    return new IncomingPhoneCallNotificationSettings.Builder()
      .icon(icon)
      .picture(picture)
      .callWaiting(callWaiting)
      .declineButtonText(declineButtonText)
      .declineButtonColor(declineButtonColor)
      .answerButtonText(answerButtonText)
      .answerButtonColor(answerButtonColor)
      .terminateAndAnswerButtonText(terminateAndAnswerButtonText)
      .terminateAndAnswerButtonColor(terminateAndAnswerButtonColor)
      .terminateButtonText(terminateButtonText)
      .terminateButtonColor(terminateButtonColor)
      .declineCallWaitingButtonText(declineCallWaitingButtonText)
      .declineCallWaitingButtonColor(declineCallWaitingButtonColor)
      .holdButtonText(holdButtonText)
      .holdButtonColor(holdButtonColor)
      .holdAndAnswerButtonText(holdAndAnswerButtonText)
      .holdAndAnswerButtonColor(holdAndAnswerButtonColor)
      .color(color)
      .duration(duration)
      .channelName(channelName)
      .channelDescription(channelDescription)
      .callingName(callingName)
      .callingNumber(callingNumber)
      .build();
  }

  private CallInProgressNotificationSettings getCallInProgressNotificationSettings(PluginCall call) {
    String icon = call.getString("icon");
    String picture = call.getString("picture");
    String terminateButtonText = call.getString("terminateButtonText");
    String terminateButtonColor = call.getString("terminateButtonColor");
    String holdButtonText = call.getString("holdButtonText");
    String holdButtonColor = call.getString("holdButtonColor");
    String color = call.getString("color");
    Integer durationValue = call.getInt("duration");
    int duration = durationValue != null ? durationValue : 0;
    String channelName = call.getString("channelName");
    String channelDescription = call.getString("channelDescription");
    String callingName = call.getString("callingName");
    String callingNumber = call.getString("callingNumber");

    validateDuration(duration);
    validateColor("color", color);
    validateColor("terminateButtonColor", terminateButtonColor);
    validateColor("holdButtonColor", holdButtonColor);

    return new CallInProgressNotificationSettings.Builder()
      .icon(icon)
      .picture(picture)
      .terminateButtonText(terminateButtonText)
      .terminateButtonColor(terminateButtonColor)
      .holdButtonText(holdButtonText)
      .holdButtonColor(holdButtonColor)
      .color(color)
      .duration(duration)
      .channelName(channelName)
      .channelDescription(channelDescription)
      .callingName(callingName)
      .callingNumber(callingNumber)
      .build();
  }

  private void validateDuration(int duration) {
    if (duration < 0) {
      throw new IllegalArgumentException("duration must be zero or greater");
    }
  }

  private void validateColor(String field, String color) {
    if (color == null) {
      return;
    }
    try {
      Color.parseColor(color);
    } catch (IllegalArgumentException exception) {
      throw new IllegalArgumentException(field + " is not a valid Android color", exception);
    }
  }
}
