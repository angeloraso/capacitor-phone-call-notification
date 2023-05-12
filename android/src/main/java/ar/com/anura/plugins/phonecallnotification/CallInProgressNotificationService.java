package ar.com.anura.plugins.phonecallnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;

public class CallInProgressNotificationService extends Service {

    public static String TAP_ACTION = "tap_call_in_progress_notification";
    public static String HOLD_ACTION = "hold_call_in_progress";
    public static String TERMINATE_ACTION = "terminate_call_in_progress";

    public static final int NOTIFICATION_ID = -234414143;

    private final IBinder mLocalBinder = new LocalBinder();
    public static CallBack callBack;

    private NotificationSettings mSettings;

    public CallInProgressNotificationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    class LocalBinder extends Binder {

        CallInProgressNotificationService getService() {
            return CallInProgressNotificationService.this;
        }
    }

    public interface CallBack {
        void onTap();
        void onHold();
        void onTerminate();
    }

    public void setCallBack(CallBack callBack) {
        CallInProgressNotificationService.callBack = callBack;
    }

    public void setSettings(NotificationSettings settings) {
        this.mSettings = settings;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = new NotificationSettings();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSettings = null;
        stopForeground(true);
        getNotificationManager().cancel(NOTIFICATION_ID);
    }

    /**
     * START_NOT_STICKY: if the process (the App) is killed with no remaining start commands to deliver,
     * then the service will be stopped instead of restarted
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void createNotification() {
        String iconName = mSettings.getIcon();
        int iconResource = getIconResId(iconName);
        if (iconResource == 0) { // If no icon at all was found, fall back to the app's icon
            iconResource = getApplicationContext().getApplicationInfo().icon;
        }

        String pictureName = mSettings.getPicture();
        int pictureResource = getIconResId(pictureName);
        if (pictureResource == 0) { // If no icon at all was found, fall back to the app's icon
            pictureResource = getApplicationContext().getApplicationInfo().icon;
        }

        final String CHANNEL_ID = "call-in-progress-notification-channel-id";
        final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_MIN;

        final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, mSettings.getChannelName(), CHANNEL_IMPORTANCE);
        notificationChannel.setDescription(mSettings.getChannelDescription());
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        // Register the channel with the system; you can't change the importance or other notification behaviors after this
        getNotificationManager().createNotificationChannel(notificationChannel);

        long milliseconds = mSettings.getDuration() * 1000;
        long startTimeMillis = System.currentTimeMillis() - milliseconds;

        Notification.Builder notificationBuilder = new Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(mSettings.getChannelName())
            // Ongoing notifications cannot be dismissed by the user
            .setOngoing(false)
            // Set the "ticker" text which is sent to accessibility services.
            .setTicker(mSettings.getChannelName())
            // To know if it is necessary to disturb the user with a notification despite having activated the "Do not interrupt" mode
            .setCategory(Notification.CATEGORY_CALL)
            // Add a timestamp pertaining to the notification
            .setWhen(startTimeMillis)
            .setShowWhen(true)
            .setUsesChronometer(true)
            // VISIBILITY_PUBLIC displays the full content of the notification
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent(TAP_ACTION))
            .setColor(Color.parseColor(mSettings.getColor()))
            // Set whether or not this notification should not bridge to other devices.
            .setLocalOnly(true);

        // Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Icon icon = Icon.createWithResource(this, pictureResource);
            Person caller = new Person.Builder()
                .setIcon(icon)
                .setName(mSettings.getCallerName() + " - " + mSettings.getCallerNumber())
                .setImportant(true)
                .build();

            Notification.CallStyle notificationStyle = Notification.CallStyle.forOngoingCall(caller, getPendingIntent(TERMINATE_ACTION));
            notificationBuilder.setStyle((notificationStyle));
            notificationBuilder.setSmallIcon(R.drawable.decline_24);
            notificationBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
        } else {
            notificationBuilder.setSmallIcon(iconResource);
            notificationBuilder.setContentText(mSettings.getCallerName() + " - " + mSettings.getCallerNumber());
            Notification.Action answerAction = new Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.hold_24),
                Html.fromHtml(
                    "<font color=\"" + Color.parseColor(mSettings.getHoldButtonColor()) + "\">" + mSettings.getHoldButtonText() + "</font>",
                    Html.FROM_HTML_MODE_LEGACY
                ),
                getPendingIntent(HOLD_ACTION)
            )
                .build();

            Notification.Action declineAction = new Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.decline_24),
                Html.fromHtml(
                    "<font color=\"" +
                    Color.parseColor(mSettings.getTerminateButtonColor()) +
                    "\">" +
                    mSettings.getTerminateButtonText() +
                    "</font>",
                    Html.FROM_HTML_MODE_LEGACY
                ),
                getPendingIntent(TERMINATE_ACTION)
            )
                .build();

            notificationBuilder.setActions(declineAction, answerAction);
        }

        Notification notification = notificationBuilder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private PendingIntent getPendingIntent(String action) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, CallInProgressNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(action);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    /**
     * Returns the shared notification service manager.
     */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * Retrieves the resource ID of the sent icon name
     *
     * @param name Name of the resource to return
     */
    private int getIconResId(String name) {
        int resId = getIconResId(name, "mipmap");

        if (resId == 0) {
            resId = getIconResId(name, "drawable");
        }

        if (resId == 0) {
            resId = getIconResId("icon", "mipmap");
        }

        if (resId == 0) {
            resId = getIconResId("icon", "drawable");
        }

        return resId;
    }

    /**
     * Retrieve resource id of the specified icon.
     *
     * @param icon The name of the icon.
     * @param type The resource type where to look for.
     *
     * @return The resource id or 0 if not found.
     */
    private int getIconResId(String icon, String type) {
        Resources res = getResources();
        String pkgName = getPackageName();

        return res.getIdentifier(icon, type, pkgName);
    }
}
