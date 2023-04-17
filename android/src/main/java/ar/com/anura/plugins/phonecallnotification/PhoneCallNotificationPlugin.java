package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "PhoneCallNotification")
public class PhoneCallNotificationPlugin extends Plugin {

    private PhoneCallNotification phoneCallNotification;

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
        }
        if (settings.getType().equals("inProgress")) {
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
