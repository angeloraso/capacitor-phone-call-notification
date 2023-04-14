package ar.com.anura.plugins.phonecallnotifications;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneCallNotification implements IncomingCallNotificationService.CallBack {

    private Context context;
    private AppCompatActivity activity;
    private NotificationSettings mSettings;
    private IncomingCallNotificationListener mListener;
    private IncomingCallNotificationService incomingCallNotificationService;
    private Boolean mIsBound = false;

    PhoneCallNotification(final AppCompatActivity activity, final Context context) {
        this.activity = activity;
        this.context = context;
        mSettings = new NotificationSettings();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            incomingCallNotificationService = ((IncomingCallNotificationService.LocalBinder) iBinder).getService();
            incomingCallNotificationService.setCallBack(IncomingCallNotification.this);
            incomingCallNotificationService.setSettings(mSettings);
            incomingCallNotificationService.createNotification();
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            incomingCallNotificationService = null;
            mIsBound = false;
        }
    };

    @Override
    public void onTap() {
        if (mListener != null) {
            mListener.onTap();
            openApp();
        }
    }

    @Override
    public void onDecline() {
        if (mListener != null) {
            mListener.onDecline();
        }
    }

    @Override
    public void onAnswer() {
        if (mListener != null) {
            mListener.onAnswer();
            openApp();
        }
    }

    @Override
    public void onTerminate() {
        if (mListener != null) {
            mListener.onTerminate();
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
        this.mListener = listener;
        startService();
    }

    public void hide() {
        stopService();
    }

    public void onResume() {
        hide();
    }

    public void onDestroy() {
        hide();
    }

    private void startService() {
        if (mIsBound || mConnection == null) {
            return;
        }

        Intent intent = new Intent(context, IncomingCallNotificationService.class);

        try {
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            context.startForegroundService(intent);
        } catch (Exception e) {}
    }

    private void stopService() {
        if (!mIsBound || mConnection == null) {
            return;
        }

        Intent intent = new Intent(context, IncomingCallNotificationService.class);
        context.unbindService(mConnection);
        context.stopService(intent);
        mIsBound = false;
    }

    private void openApp() {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        activity.startActivity(intent);
    }
}
