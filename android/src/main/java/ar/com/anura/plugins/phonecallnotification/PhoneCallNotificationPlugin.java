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
                        JSObject res = new JSObject();
                        res.put("response", "tap");
                        call.resolve(res);
                    }

                    @Override
                    public void onDecline() {
                        JSObject res = new JSObject();
                        res.put("response", "decline");
                        call.resolve(res);
                    }

                    @Override
                    public void onAnswer() {
                        JSObject res = new JSObject();
                        res.put("response", "answer");
                        call.resolve(res);
                    }

                    @Override
                    public void onTerminate() {
                        JSObject res = new JSObject();
                        res.put("response", "terminate");
                        call.resolve(res);
                    }
                }
            );
        } else if (settings.getType().equals("inProgress")) {
            phoneCallNotification.showCallInProgressNotification(
                settings,
                new CallInProgressNotificationListener() {
                    @Override
                    public void onTap() {
                        JSObject res = new JSObject();
                        res.put("response", "tap");
                        call.resolve(res);
                    }

                    @Override
                    public void onHold() {
                        JSObject res = new JSObject();
                        res.put("response", "hold");
                        call.resolve(res);
                    }

                    @Override
                    public void onTerminate() {
                        JSObject res = new JSObject();
                        res.put("response", "terminate");
                        call.resolve(res);
                    }
                }
            );
        } else {
            call.reject("Phone call notification plugin error: Notification type is required");
        }
    }

    @PluginMethod
    public void hide(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Phone call notification plugin error: App is finishing");
            return;
        }

        if (call.hasOption("type")) {
            String type = call.getString("type");
            if (type.equals("incoming")) {
                phoneCallNotification.hideIncomingCall();
            } else if (type.equals("inProgress")) {
                phoneCallNotification.hideCallInProgress();
            }
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
        if (call.hasOption("type")) {
            settings.setType((call.getString("type")));
        }
        if (call.hasOption("callerName")) {
            settings.setCallerName((call.getString("callerName")));
        }
        if (call.hasOption("callerNumber")) {
            settings.setCallerNumber((call.getString("callerNumber")));
        }
        if (call.hasOption("icon")) {
            settings.setIcon((call.getString("icon")));
        }
        if (call.hasOption("thereIsACallInProgress")) {
            settings.setThereIsACallInProgress((call.getBoolean("thereIsACallInProgress")));
        }
        if (call.hasOption("declineButtonText")) {
            settings.setDeclineButtonText((call.getString("declineButtonText")));
        }
        if (call.hasOption("answerButtonText")) {
            settings.setAnswerButtonText((call.getString("answerButtonText")));
        }
        if (call.hasOption("terminateAndAnswerButtonText")) {
            settings.setTerminateAndAnswerButtonText((call.getString("terminateAndAnswerButtonText")));
        }
        if (call.hasOption("terminateButtonText")) {
            settings.setTerminateButtonText((call.getString("terminateButtonText")));
        }
        if (call.hasOption("holdButtonText")) {
            settings.setHoldButtonText((call.getString("holdButtonText")));
        }
        if (call.hasOption("declineSecondCallButtonText")) {
            settings.setDeclineSecondCallButtonText((call.getString("declineSecondCallButtonText")));
        }
        if (call.hasOption("holdAndAnswerButtonText")) {
            settings.setHoldAndAnswerButtonText((call.getString("holdAndAnswerButtonText")));
        }
        if (call.hasOption("declineButtonColor")) {
            settings.setDeclineButtonColor((call.getString("declineButtonColor")));
        }
        if (call.hasOption("answerButtonColor")) {
            settings.setAnswerButtonColor((call.getString("answerButtonColor")));
        }
        if (call.hasOption("terminateAndAnswerButtonColor")) {
            settings.setTerminateAndAnswerButtonColor((call.getString("terminateAndAnswerButtonColor")));
        }
        if (call.hasOption("terminateButtonColor")) {
            settings.setTerminateButtonColor((call.getString("terminateButtonColor")));
        }
        if (call.hasOption("holdButtonColor")) {
            settings.setHoldButtonColor((call.getString("holdButtonColor")));
        }
        if (call.hasOption("declineSecondCallButtonColor")) {
            settings.setDeclineSecondCallButtonColor((call.getString("declineSecondCallButtonColor")));
        }
        if (call.hasOption("holdAndAnswerButtonColor")) {
            settings.setHoldAndAnswerButtonColor((call.getString("holdAndAnswerButtonColor")));
        }
        if (call.hasOption("color")) {
            settings.setColor((call.getString("color")));
        }
        if (call.hasOption("duration")) {
            settings.setDuration((call.getInt("duration")));
        }
        if (call.hasOption("picture")) {
            settings.setPicture((call.getString("picture")));
        }
        if (call.hasOption("channelName")) {
            settings.setChannelName((call.getString("channelName")));
        }
        if (call.hasOption("channelDescription")) {
            settings.setChannelDescription((call.getString("channelDescription")));
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
