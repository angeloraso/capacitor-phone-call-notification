package ar.com.anura.plugins.phonecallnotification;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
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
        alias = PhoneCallNotificationPlugin.PHONE_CALL_NOTIFICATIONS
    )
)
public class PhoneCallNotificationPlugin extends Plugin {

    private PhoneCallNotification phoneCallNotification;

    static final String PHONE_CALL_NOTIFICATIONS = "display";

    public void load() {
        AppCompatActivity activity = getActivity();
        Context context = getContext();
        phoneCallNotification = new PhoneCallNotification(activity, context);
    }

    private void onPhoneCallNotificationEvent(String response) {
        JSObject res = new JSObject();
        res.put("response", response);
        bridge.triggerWindowJSEvent("response");
        notifyListeners("response", res);
    }

    @PluginMethod
    public void show(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Phone call notification plugin error: App is finishing");
            return;
        }

        NotificationSettings settings = getSettings(call);
        if (settings.getType().equals("incoming")) {
            phoneCallNotification.showIncomingCallNotification(
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
        } else if (settings.getType().equals("inProgress")) {
            phoneCallNotification.showCallInProgressNotification(
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
        } else {
            call.reject("Phone call notification plugin error: Notification type is required");
            return;
        }

        call.resolve();
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
            phoneCallNotification.hideIncomingCall();
        } else if (type.equals("inProgress")) {
            phoneCallNotification.hideCallInProgress();
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
        if (phoneCallNotification.areNotificationsEnabled()) {
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

    /**
     * Called when the activity will start interacting with the user.
     */
    @Override
    public void handleOnResume() {
        phoneCallNotification.onResume();
    }

    /**
     * Called when the activity will be destroyed.
     */
    @Override
    public void handleOnDestroy() {
        phoneCallNotification.onDestroy();
    }
}
