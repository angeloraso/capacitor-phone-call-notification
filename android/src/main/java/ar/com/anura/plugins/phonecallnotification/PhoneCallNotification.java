package ar.com.anura.plugins.phonecallnotification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneCallNotification implements IncomingCallNotificationService.CallBack, CallInProgressNotificationService.CallBack {

    private Context context;
    private AppCompatActivity activity;
    private NotificationSettings mSettings;
    private IncomingCallNotificationListener incomingCallNotificationListener;

    private CallInProgressNotificationListener callInProgressNotificationListener;
    private IncomingCallNotificationService incomingCallNotificationService;
    private Boolean mIncomingCallServiceIsBound = false;
    private Boolean mCallInProgressServiceIsBound = false;
    private ServiceConnection mIncomingCallServiceConnection;
    private ServiceConnection mCallInProgressServiceConnection;

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
            openApp();
        }
    }

    @Override
    public void onTerminate() {
        if (callInProgressNotificationListener != null) {
            callInProgressNotificationListener.onTerminate();
            openApp();
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
                    mIncomingCallServiceIsBound = true;
                }

                public void onServiceDisconnected(ComponentName className) {
                    incomingCallNotificationService = null;
                    mIncomingCallServiceIsBound = false;
                }
            };

        Intent intent = new Intent(context, IncomingCallNotificationService.class);

        try {
            context.bindService(intent, mIncomingCallServiceConnection, Context.BIND_AUTO_CREATE);
            context.startForegroundService(intent);
        } catch (Exception e) {}
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
                    incomingCallNotificationService = ((IncomingCallNotificationService.LocalBinder) iBinder).getService();
                    incomingCallNotificationService.setCallBack(PhoneCallNotification.this);
                    incomingCallNotificationService.setSettings(mSettings);
                    incomingCallNotificationService.createNotification();
                    mCallInProgressServiceIsBound = true;
                }

                public void onServiceDisconnected(ComponentName className) {
                    incomingCallNotificationService = null;
                    mCallInProgressServiceIsBound = false;
                }
            };

        Intent intent = new Intent(context, CallInProgressNotificationService.class);

        try {
            context.bindService(intent, mCallInProgressServiceConnection, Context.BIND_AUTO_CREATE);
            context.startForegroundService(intent);
        } catch (Exception e) {}
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

    private void stopService() {
        stopIncomingCallService();
        stopCallInProgressService();
    }

    private void stopIncomingCallService() {
        if (mIncomingCallServiceIsBound && mIncomingCallServiceConnection != null) {
            Intent intent = new Intent(context, IncomingCallNotificationService.class);
            context.unbindService(mIncomingCallServiceConnection);
            context.stopService(intent);
            mIncomingCallServiceIsBound = false;
        }
    }

    private void stopCallInProgressService() {
        if (mCallInProgressServiceIsBound && mCallInProgressServiceConnection != null) {
            Intent intent = new Intent(context, CallInProgressNotificationService.class);
            context.unbindService(mCallInProgressServiceConnection);
            context.stopService(intent);
            mCallInProgressServiceIsBound = false;
        }
    }

    private void openApp() {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        activity.startActivity(intent);
    }
}
