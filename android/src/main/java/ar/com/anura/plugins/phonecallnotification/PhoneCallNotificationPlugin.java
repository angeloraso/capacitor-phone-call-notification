package ar.com.anura.plugins.phonecallnotification;

import android.Manifest;
import android.os.Build;

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
public class PhoneCallNotificationPlugin extends Plugin {
  static final String PHONE_CALL_NOTIFICATIONS_PERMISSION = "display";

  public void load() {
    PhoneCallNotification.initialize(getActivity());
  }

  private void onPhoneCallNotificationEvent(String response) {
    JSObject res = new JSObject();
    res.put("response", response);
    bridge.triggerWindowJSEvent("response");
    notifyListeners("response", res);
  }

  @PluginMethod
  public void showIncomingPhoneCallNotification(PluginCall call) {
    if (getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

    IncomingPhoneCallNotificationSettings settings = getIncomingPhoneCallNotificationSettings(call);

    PhoneCallNotification.showIncomingCallNotification(
      settings,
      new IncomingCallNotificationListener() {
        @Override
        public void onTap() {
          onPhoneCallNotificationEvent("tap");
        }

        @Override
        public void onDecline() {
          onPhoneCallNotificationEvent("decline");
        }

        @Override
        public void onAnswer() {
          onPhoneCallNotificationEvent("answer");
        }

        @Override
        public void onTerminate() {
          onPhoneCallNotificationEvent("terminate");
        }
      }
    );

    call.resolve();
  }

  @PluginMethod
  public void showCallInProgressNotification(PluginCall call) {
    if (getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

    CallInProgressNotificationSettings settings = getCallInProgressNotificationSettings(call);

    PhoneCallNotification.showCallInProgressNotification(
      settings,
      new CallInProgressNotificationListener() {
        @Override
        public void onTap() {
          onPhoneCallNotificationEvent("tap");
        }

        @Override
        public void onHold() {
          onPhoneCallNotificationEvent("hold");
        }

        @Override
        public void onTerminate() {
          onPhoneCallNotificationEvent("terminate");
        }
      }
    );

    call.resolve();
  }

  @PluginMethod
  public void hideIncomingPhoneCallNotification(PluginCall call) {
    if (getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

    PhoneCallNotification.hideIncomingPhoneCallNotification();
    call.resolve();
  }

  @PluginMethod
  public void hideCallInProgressNotification(PluginCall call) {
    if (getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

    PhoneCallNotification.hideCallInProgressNotification();
    call.resolve();
  }

  @PluginMethod
  public void hideAll(PluginCall call) {
    if (getActivity().isFinishing()) {
      call.reject("Phone call notification plugin error: App is finishing");
      return;
    }

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
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || getPermissionState(PHONE_CALL_NOTIFICATIONS_PERMISSION) == PermissionState.GRANTED) {
      notificationPermissionCallback(call);
    } else {
      requestPermissionForAlias(PHONE_CALL_NOTIFICATIONS_PERMISSION, call, "notificationPermissionCallback");
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
}
