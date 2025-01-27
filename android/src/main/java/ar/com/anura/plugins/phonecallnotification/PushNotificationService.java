package ar.com.anura.plugins.phonecallnotification;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = "PushNotificationService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "FCM remote message: " + remoteMessage);
        PhoneCallNotificationPlugin.onRemoteMessage(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM token: " + token);
        PhoneCallNotificationPlugin.onNewToken(token);
    }
}
