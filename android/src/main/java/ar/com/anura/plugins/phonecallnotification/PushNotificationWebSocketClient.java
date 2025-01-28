package ar.com.anura.plugins.phonecallnotification;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class PushNotificationWebSocketClient extends WebSocketClient {

    private static final String TAG = "PushNotificationWebSocketClient";

    public PushNotificationWebSocketClient(String serverUrl) throws URISyntaxException {
        super(new URI(serverUrl));
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Log.d(TAG, "Push notification web socket client onOpen: " + handshake);
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, "Push notification web socket client onMessage: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "Push notification web socket client onClose: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        Log.d(TAG, "Push notification web socket client onError: " + ex.getMessage());
    }
}