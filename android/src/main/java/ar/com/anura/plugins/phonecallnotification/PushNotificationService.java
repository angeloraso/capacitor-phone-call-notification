package ar.com.anura.plugins.phonecallnotification;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URISyntaxException;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = "PushNotificationService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "FCM remote message: " + remoteMessage);

        PhoneCallNotificationPlugin.onRemoteMessage(remoteMessage);

        if (PhoneCallNotification.WS_SERVER_URL != null && "REGISTER".equals(remoteMessage.getData().get("type"))) {
            try {
                PushNotificationWebSocketClient client = new PushNotificationWebSocketClient(PhoneCallNotification.WS_SERVER_URL);
                client.connect();
                client.send("200 OK");
                client.close();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else if ("INVITE".equals(remoteMessage.getData().get("type"))){
            PhoneCallNotificationPlugin pushPlugin = PhoneCallNotificationPlugin.getPhoneCallNotificationInstance();
            if (pushPlugin != null ) {
                NotificationSettings settings = new NotificationSettings();
                pushPlugin.showIncomingCallNotification(settings);
            }
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM token: " + token);
        PhoneCallNotificationPlugin.onNewToken(token);
    }
}
