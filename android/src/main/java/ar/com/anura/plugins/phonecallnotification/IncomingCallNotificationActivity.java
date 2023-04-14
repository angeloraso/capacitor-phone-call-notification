package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.IncomingCallNotificationService.ANSWER_ACTION;
import static ar.com.anura.plugins.phonecallnotification.IncomingCallNotificationService.DECLINE_ACTION;
import static ar.com.anura.plugins.phonecallnotification.IncomingCallNotificationService.TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.IncomingCallNotificationService.TERMINATE_ACTION;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingCallNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (IncomingCallNotificationService.callBack == null) {
            return;
        }

        if (TAP_ACTION.equals(intent.getAction())) {
            IncomingCallNotificationService.callBack.onTap();
        } else if (DECLINE_ACTION.equals(intent.getAction())) {
            IncomingCallNotificationService.callBack.onDecline();
        } else if (ANSWER_ACTION.equals(intent.getAction())) {
            IncomingCallNotificationService.callBack.onAnswer();
        } else if (TERMINATE_ACTION.equals(intent.getAction())) {
            IncomingCallNotificationService.callBack.onTerminate();
        }

        finish();
    }
}
