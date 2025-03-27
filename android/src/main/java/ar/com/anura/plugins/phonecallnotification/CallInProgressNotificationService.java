package ar.com.anura.plugins.phonecallnotification;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE;

import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_HOLD_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_NOTIFICATION_ID;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_TERMINATE_ACTION;

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
import android.os.Build;
import android.os.IBinder;
import android.text.Html;

public class CallInProgressNotificationService extends Service {
    public CallInProgressNotificationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        getNotificationManager().cancel(CALL_IN_PROGRESS_NOTIFICATION_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        return START_STICKY;
    }

    public void createNotification() {
        NotificationSettings settings = PhoneCallNotification.callInProgressNotificationSettings;
        String iconName = settings.getIcon();
        int iconResource = getIconResId(iconName);
        if (iconResource == 0) { // If no icon at all was found, fall back to the app's icon
        iconResource = getApplicationContext().getApplicationInfo().icon;
        }

        String pictureName = settings.getPicture();
        int pictureResource = getIconResId(pictureName);
        if (pictureResource == 0) { // If no icon at all was found, fall back to the app's icon
        pictureResource = getApplicationContext().getApplicationInfo().icon;
        }

        final String CHANNEL_ID = "call-in-progress-notification-channel-id";
        final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_MIN;

        final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, settings.getChannelName(), CHANNEL_IMPORTANCE);
        notificationChannel.setDescription(settings.getChannelDescription());
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        // Register the channel with the system; you can't change the importance or other notification behaviors after this
        getNotificationManager().createNotificationChannel(notificationChannel);

        long milliseconds = settings.getDuration() * 1000L;
        long startTimeMillis = System.currentTimeMillis() - milliseconds;

        Notification.Builder notificationBuilder = new Notification.Builder(this, CHANNEL_ID)
        .setContentTitle(settings.getChannelName())
        // Ongoing notifications cannot be dismissed by the user
        .setOngoing(false)
        // Set the "ticker" text which is sent to accessibility services.
        .setTicker(settings.getChannelName())
        // To know if it is necessary to disturb the user with a notification despite having activated the "Do not interrupt" mode
        .setCategory(Notification.CATEGORY_CALL)
        // Add a timestamp pertaining to the notification
        .setWhen(startTimeMillis)
        .setShowWhen(true)
        .setUsesChronometer(true)
        // VISIBILITY_PUBLIC displays the full content of the notification
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setContentIntent(getPendingIntent(CALL_IN_PROGRESS_TAP_ACTION))
        .setColor(Color.parseColor(settings.getColor()))
        // Set whether or not this notification should not bridge to other devices.
        .setLocalOnly(true);

        // Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Icon icon = Icon.createWithResource(this, pictureResource);
        Person caller = new Person.Builder()
            .setIcon(icon)
            .setName(settings.getCallerName() + " - " + settings.getCallerNumber())
            .setImportant(true)
            .build();

        Notification.CallStyle notificationStyle = Notification.CallStyle.forOngoingCall(caller, getPendingIntent(CALL_IN_PROGRESS_TERMINATE_ACTION));
        notificationBuilder.setStyle((notificationStyle));
        notificationBuilder.setSmallIcon(getIconResId("answer", "drawable"));
        notificationBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
        } else {
        notificationBuilder.setSmallIcon(iconResource);
        notificationBuilder.setContentText(settings.getCallerName() + " - " + settings.getCallerNumber());
        Notification.Action answerAction = new Notification.Action.Builder(
            Icon.createWithResource(this, getIconResId("hold", "drawable")),
            Html.fromHtml(
            "<font color=\"" + Color.parseColor(settings.getHoldButtonColor()) + "\">" + settings.getHoldButtonText() + "</font>",
            Html.FROM_HTML_MODE_LEGACY
            ),
            getPendingIntent(CALL_IN_PROGRESS_HOLD_ACTION)
        )
            .build();

        Notification.Action declineAction = new Notification.Action.Builder(
            Icon.createWithResource(this, getIconResId("decline", "drawable")),
            Html.fromHtml(
            "<font color=\"" +
                Color.parseColor(settings.getTerminateButtonColor()) +
                "\">" +
              settings.getTerminateButtonText() +
                "</font>",
            Html.FROM_HTML_MODE_LEGACY
            ),
            getPendingIntent(CALL_IN_PROGRESS_TERMINATE_ACTION)
        )
            .build();

        notificationBuilder.setActions(declineAction, answerAction);
        }

        Notification notification = notificationBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        startForeground(CALL_IN_PROGRESS_NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SHORT_SERVICE);
        } else {
        startForeground(CALL_IN_PROGRESS_NOTIFICATION_ID, notification);
        }
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
