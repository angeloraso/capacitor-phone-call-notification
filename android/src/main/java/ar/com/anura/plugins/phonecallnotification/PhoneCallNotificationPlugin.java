package ar.com.anura.plugins.phonecallnotification;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginHandle;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

@CapacitorPlugin(
    name = "PhoneCallNotification",
    permissions = @Permission(
        strings = { Manifest.permission.POST_NOTIFICATIONS },
        alias = PhoneCallNotificationPlugin.PHONE_CALL_NOTIFICATIONS
    )
)
public class PhoneCallNotificationPlugin extends Plugin {

    public static Bridge staticBridge = null;

    private static boolean isAppInForeground = false;

    static final String PHONE_CALL_NOTIFICATIONS = "display";

    static String pushNotificationResponse = null;

    public void load() {
        staticBridge = this.bridge;
        PhoneCallNotification.initialize(getActivity());
    }

    private void onPhoneCallNotificationEvent(String response) {
        JSObject res = new JSObject();
        res.put("response", response);
        bridge.triggerWindowJSEvent("response");
        notifyListeners("response", res);
    }

    private void onPushNotificationTokenEvent(String token) {
        JSObject res = new JSObject();
        res.put("value", token);
        bridge.triggerWindowJSEvent("pushNotificationToken");
        notifyListeners("pushNotificationToken", res);
    }

    private void onPushNotificationDataEvent(Map<String, String> data) {
        JSObject res = new JSObject();
        res.put("data", data);
        bridge.triggerWindowJSEvent("pushNotificationData");
        notifyListeners("pushNotificationData", res);
    }

    public static void onNewToken(String token) {
        PhoneCallNotificationPlugin pushPlugin = PhoneCallNotificationPlugin.getPhoneCallNotificationInstance();
        if (pushPlugin != null) {
            pushPlugin.onPushNotificationTokenEvent(token);
        }
    }

    public static void onMessageReceived(RemoteMessage remoteMessage) {
        PhoneCallNotificationPlugin pushPlugin = PhoneCallNotificationPlugin.getPhoneCallNotificationInstance();
        if (pushPlugin != null ) {
            pushPlugin.onPushNotificationDataEvent(remoteMessage.getData());
        }
    }

    public static PhoneCallNotificationPlugin getPhoneCallNotificationInstance() {
        if (staticBridge != null && staticBridge.getWebView() != null) {
            PluginHandle handle = staticBridge.getPlugin("PhoneCallNotification");
            if (handle == null) {
                return null;
            }
            return (PhoneCallNotificationPlugin) handle.getInstance();
        }
        return null;
    }

    @PluginMethod
    public void registerPushNotifications(PluginCall call) {
        try {
            if (getActivity().isFinishing()) {
                call.reject("Phone call notification plugin error: App is finishing");
                return;
            }

            PhoneCallNotification.registerPushNotifications(getSettings(call), this::onPushNotificationTokenEvent);
            call.resolve();
        } catch (Exception exception) {
            call.reject(exception.getMessage());
        }
    }

    @PluginMethod
    public void getPushNotificationResponse(PluginCall call) {
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        JSObject res = new JSObject();
        if (extras != null && pushNotificationResponse == null) {
            String response = (String) extras.get("response");
            pushNotificationResponse = response;
            res.put("response", response);
        } else {
            res.put("response", "");
        }

        call.resolve(res);
    }

    @PluginMethod
    public void unregisterPushNotifications(PluginCall call) {
        try {
            if (getActivity().isFinishing()) {
                call.reject("Phone call notification plugin error: App is finishing");
                return;
            }

            PhoneCallNotification.unregisterPushNotifications();
            call.resolve();
        } catch (Exception exception) {
            call.reject(exception.getMessage());
        }
    }

    @PluginMethod
    public void show(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Phone call notification plugin error: App is finishing");
            return;
        }

        NotificationSettings settings = getSettings(call);
        if (settings.getType().equals("incoming")) {
            showIncomingCallNotification(settings);
        } else if (settings.getType().equals("inProgress")) {
            showCallInProgressNotification(settings);
        } else {
            call.reject("Phone call notification plugin error: Notification type is required");
            return;
        }

        call.resolve();
    }

    public void showIncomingCallNotification(NotificationSettings settings) {
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
    }

    public void showCallInProgressNotification(NotificationSettings settings) {
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
    }

    @PluginMethod
    public void hide(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Phone call notification plugin error: App is finishing");
            return;
        }

        String type = call.getString("type");
        if (type == null) {
            call.reject("The notification type is required");
            return;
        }

        if (type.equals("incoming")) {
            PhoneCallNotification.hideIncomingCall();
        } else if (type.equals("inProgress")) {
            PhoneCallNotification.hideCallInProgress();
        }

        call.resolve();
    }

    @PluginMethod
    public void checkPermissions(PluginCall call) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            JSObject permissionsResultJSON = new JSObject();
            permissionsResultJSON.put(PHONE_CALL_NOTIFICATIONS, getNotificationPermissionText());
            call.resolve(permissionsResultJSON);
        } else {
            super.checkPermissions(call);
        }
    }

    @PluginMethod
    public void requestPermissions(PluginCall call) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            JSObject permissionsResultJSON = new JSObject();
            permissionsResultJSON.put(PHONE_CALL_NOTIFICATIONS, getNotificationPermissionText());
            call.resolve(permissionsResultJSON);
        } else {
            if (getPermissionState(PHONE_CALL_NOTIFICATIONS) != PermissionState.GRANTED) {
                requestPermissionForAlias(PHONE_CALL_NOTIFICATIONS, call, "permissionsCallback");
            }
        }
    }

    @PermissionCallback
    private void permissionsCallback(PluginCall call) {
        JSObject permissionsResultJSON = new JSObject();
        permissionsResultJSON.put(PHONE_CALL_NOTIFICATIONS, getNotificationPermissionText());
        call.resolve(permissionsResultJSON);
    }

    private String getNotificationPermissionText() {
        if (PhoneCallNotification.areNotificationsEnabled()) {
            return "granted";
        } else {
            return "denied";
        }
    }

    private NotificationSettings getSettings(PluginCall call) {
        NotificationSettings settings = new NotificationSettings();

        String type = call.getString("type");
        if (type != null) {
            settings.setType(type);
        }

        String callerName = call.getString("callerName");
        if (callerName != null) {
            settings.setCallerName(callerName);
        }

        String callerNumber = call.getString("callerNumber");
        if (callerNumber != null) {
            settings.setCallerNumber(callerNumber);
        }

        String icon = call.getString("icon");
        if (icon != null) {
            settings.setIcon(icon);
        }

        Boolean thereIsACallInProgress = call.getBoolean("thereIsACallInProgress");
        if (thereIsACallInProgress != null) {
            settings.setThereIsACallInProgress(thereIsACallInProgress);
        }

        String declineButtonText = call.getString("declineButtonText");
        if (declineButtonText != null) {
            settings.setDeclineButtonText(declineButtonText);
        }

        String answerButtonText = call.getString("answerButtonText");
        if (answerButtonText != null) {
            settings.setAnswerButtonText(answerButtonText);
        }

        String terminateAndAnswerButtonText = call.getString("terminateAndAnswerButtonText");
        if (terminateAndAnswerButtonText != null) {
            settings.setTerminateAndAnswerButtonText(terminateAndAnswerButtonText);
        }

        String terminateButtonText = call.getString("terminateButtonText");
        if (terminateButtonText != null) {
            settings.setTerminateButtonText(terminateButtonText);
        }

        String holdButtonText = call.getString("holdButtonText");
        if (holdButtonText != null) {
            settings.setHoldButtonText(holdButtonText);
        }

        String declineSecondCallButtonText = call.getString("declineSecondCallButtonText");
        if (declineSecondCallButtonText != null) {
            settings.setDeclineSecondCallButtonText(declineSecondCallButtonText);
        }

        String holdAndAnswerButtonText = call.getString("holdAndAnswerButtonText");
        if (holdAndAnswerButtonText != null) {
            settings.setHoldAndAnswerButtonText(holdAndAnswerButtonText);
        }

        String declineButtonColor = call.getString("declineButtonColor");
        if (declineButtonColor != null) {
            settings.setDeclineButtonColor(declineButtonColor);
        }

        String answerButtonColor = call.getString("answerButtonColor");
        if (answerButtonColor != null) {
            settings.setAnswerButtonColor(answerButtonColor);
        }

        String terminateAndAnswerButtonColor = call.getString("terminateAndAnswerButtonColor");
        if (terminateAndAnswerButtonColor != null) {
            settings.setTerminateAndAnswerButtonColor(terminateAndAnswerButtonColor);
        }

        String terminateButtonColor = call.getString("terminateButtonColor");
        if (terminateButtonColor != null) {
            settings.setTerminateButtonColor(terminateButtonColor);
        }

        String holdButtonColor = call.getString("holdButtonColor");
        if (holdButtonColor != null) {
            settings.setHoldButtonColor(holdButtonColor);
        }

        String declineSecondCallButtonColor = call.getString("declineSecondCallButtonColor");
        if (declineSecondCallButtonColor != null) {
            settings.setDeclineSecondCallButtonColor(declineSecondCallButtonColor);
        }

        String holdAndAnswerButtonColor = call.getString("holdAndAnswerButtonColor");
        if (holdAndAnswerButtonColor != null) {
            settings.setHoldAndAnswerButtonColor(holdAndAnswerButtonColor);
        }

        String color = call.getString("color");
        if (color != null) {
            settings.setColor(color);
        }

        Integer duration = call.getInt("duration");
        if (duration != null && duration >= 0) {
            settings.setDuration(duration);
        }

        String picture = call.getString("picture");
        if (picture != null) {
            settings.setPicture(picture);
        }

        String channelName = call.getString("channelName");
        if (channelName != null) {
            settings.setChannelName(channelName);
        }

        String channelDescription = call.getString("channelDescription");
        if (channelDescription != null) {
            settings.setChannelDescription(channelDescription);
        }

        return settings;
    }

    public static boolean isAppInForeground() {
        return isAppInForeground;
    }

    /**
     * Called when the activity will start interacting with the user.
     */
    @Override
    public void handleOnResume() {
        PhoneCallNotification.onResume();
        isAppInForeground = true;
    }

    @Override
    public void handleOnPause() {
        isAppInForeground = false;
    }

    /**
     * Called when the activity will be destroyed.
     */
    @Override
    public void handleOnDestroy() {
        PhoneCallNotification.onDestroy();
        isAppInForeground = false;
    }
}
