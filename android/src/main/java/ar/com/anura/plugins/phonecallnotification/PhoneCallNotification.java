package ar.com.anura.plugins.phonecallnotification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;
import java.util.function.Consumer;

public class PhoneCallNotification implements IncomingCallNotificationService.CallBack, CallInProgressNotificationService.CallBack {

    private final Context context;
    private final AppCompatActivity activity;
    private NotificationSettings mSettings;
    private IncomingCallNotificationListener incomingCallNotificationListener;

    private CallInProgressNotificationListener callInProgressNotificationListener;
    private IncomingCallNotificationService incomingCallNotificationService;
    private CallInProgressNotificationService callInProgressNotificationService;
    private Boolean mShouldUnbindIncomingCallService = false;
    private Boolean mShouldUnbindCallInProgressService = false;
    private ServiceConnection mIncomingCallServiceConnection;
    private ServiceConnection mCallInProgressServiceConnection;

    public static String WS_SERVER_URL = null;

    PhoneCallNotification(final AppCompatActivity activity, final Context context) {
        this.activity = activity;
        this.context = context;
        mSettings = new NotificationSettings();
    }

    @Override
    public void onIncomingCallNotificationTap() {
        if (incomingCallNotificationListener != null) {
            incomingCallNotificationListener.onTap();
            openApp();
        }
    }

    @Override
    public void onDecline() {
        if (incomingCallNotificationListener != null) {
            incomingCallNotificationListener.onDecline();
        }
    }

    @Override
    public void onAnswer() {
        if (incomingCallNotificationListener != null) {
            incomingCallNotificationListener.onAnswer();
            openApp();
        }
    }

    @Override
    public void onIncomingCallNotificationTerminate() {
        if (incomingCallNotificationListener != null) {
            incomingCallNotificationListener.onTerminate();
            openApp();
        }
    }

    @Override
    public void onTap() {
        if (callInProgressNotificationListener != null) {
            callInProgressNotificationListener.onTap();
            openApp();
        }
    }

    @Override
    public void onHold() {
        if (callInProgressNotificationListener != null) {
            callInProgressNotificationListener.onHold();
        }
    }

    @Override
    public void onTerminate() {
        if (callInProgressNotificationListener != null) {
            callInProgressNotificationListener.onTerminate();
        }
    }

    /**
     * Show the incoming call notification
     *
     * @param settings Settings used to show the incoming call notification
     * @param listener A listener to handle user actions
     */
    public void showIncomingCallNotification(final NotificationSettings settings, final IncomingCallNotificationListener listener) {
        this.mSettings = settings;
        this.incomingCallNotificationListener = listener;
        mIncomingCallServiceConnection =
            new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder iBinder) {
                    incomingCallNotificationService = ((IncomingCallNotificationService.LocalBinder) iBinder).getService();
                    incomingCallNotificationService.setCallBack(PhoneCallNotification.this);
                    incomingCallNotificationService.setSettings(mSettings);
                    incomingCallNotificationService.createNotification();
                }

                public void onServiceDisconnected(ComponentName className) {
                    incomingCallNotificationService = null;
                }
            };


          Intent intent = new Intent(context, IncomingCallNotificationService.class);

          context.bindService(intent, mIncomingCallServiceConnection, Context.BIND_AUTO_CREATE);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new Handler(Looper.getMainLooper()).post(() -> {
              context.startForegroundService(intent);
              mShouldUnbindIncomingCallService = true;
            });
          } else {
            context.startService(intent);
            mShouldUnbindIncomingCallService = true;
          }
    }

    /**
     * Show the call in progress notification
     *
     * @param settings Settings used to show the incoming call notification
     * @param listener A listener to handle user actions
     */
    public void showCallInProgressNotification(final NotificationSettings settings, final CallInProgressNotificationListener listener) {
        this.mSettings = settings;
        this.callInProgressNotificationListener = listener;
        mCallInProgressServiceConnection =
            new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder iBinder) {
                    callInProgressNotificationService = ((CallInProgressNotificationService.LocalBinder) iBinder).getService();
                    callInProgressNotificationService.setCallBack(PhoneCallNotification.this);
                    callInProgressNotificationService.setSettings(mSettings);
                    callInProgressNotificationService.createNotification();
                }

                public void onServiceDisconnected(ComponentName className) {
                    callInProgressNotificationService = null;
                }
            };

        Intent intent = new Intent(context, CallInProgressNotificationService.class);

        context.bindService(intent, mCallInProgressServiceConnection, Context.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          new Handler(Looper.getMainLooper()).post(() -> {
            context.startForegroundService(intent);
            mShouldUnbindCallInProgressService = true;
          });
        } else {
          context.startService(intent);
          mShouldUnbindCallInProgressService = true;
        }
    }

    public void hideIncomingCall() {
        stopIncomingCallService();
    }

    public void hideCallInProgress() {
        stopCallInProgressService();
    }

    public void onResume() {
        stopService();
    }

    public void onDestroy() {
        stopService();
    }

    public boolean areNotificationsEnabled() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        return notificationManager.areNotificationsEnabled();
    }

    public void registerToPushNotifications(String wsServerURL,  Consumer<String> onPushNotificationTokenEvent) {
        WS_SERVER_URL = wsServerURL;
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging
                .getInstance()
                .getToken()
                .addOnCompleteListener(
                    task -> {
                        if (!task.isSuccessful()) {
                            try {
                                throw new Exception(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            onPushNotificationTokenEvent.accept(task.getResult());
                        }
                    }
                );
    }

    public void unregisterFromPushNotifications() {
        try {
            WS_SERVER_URL = null;
            FirebaseMessaging.getInstance().setAutoInitEnabled(false);
            FirebaseMessaging.getInstance().deleteToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stopService() {
        stopIncomingCallService();
        stopCallInProgressService();
    }

    private void stopIncomingCallService() {
        if (mShouldUnbindIncomingCallService && mIncomingCallServiceConnection != null) {
            Intent intent = new Intent(context, IncomingCallNotificationService.class);
            context.unbindService(mIncomingCallServiceConnection);
            context.stopService(intent);
            mShouldUnbindIncomingCallService = false;
        }
    }

    private void stopCallInProgressService() {
        if (mShouldUnbindCallInProgressService && mCallInProgressServiceConnection != null) {
            Intent intent = new Intent(context, CallInProgressNotificationService.class);
            context.unbindService(mCallInProgressServiceConnection);
            context.stopService(intent);
            mShouldUnbindCallInProgressService = false;
        }
    }

    private void openApp() {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        activity.startActivity(intent);
    }
}
