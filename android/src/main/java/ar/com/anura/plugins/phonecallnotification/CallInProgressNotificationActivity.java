package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_HOLD_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_TERMINATE_ACTION;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CallInProgressNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (CALL_IN_PROGRESS_TAP_ACTION.equals(intent.getAction())) {
          PhoneCallNotification.callInProgressNotificationListener.onTap();
        } else if (CALL_IN_PROGRESS_HOLD_ACTION.equals(intent.getAction())) {
          PhoneCallNotification.callInProgressNotificationListener.onHold();
        } else if (CALL_IN_PROGRESS_TERMINATE_ACTION.equals(intent.getAction())) {
          PhoneCallNotification.callInProgressNotificationListener.onTerminate();
        }

        finish();
    }
}
