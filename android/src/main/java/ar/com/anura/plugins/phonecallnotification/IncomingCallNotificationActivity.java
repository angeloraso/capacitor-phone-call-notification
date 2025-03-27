package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_DECLINE_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_ANSWER_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_TERMINATE_ACTION;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingCallNotificationActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();

    if (INCOMING_CALL_TAP_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.incomingCallNotificationListener.onTap();
    } else if (INCOMING_CALL_DECLINE_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.incomingCallNotificationListener.onDecline();
    } else if (INCOMING_CALL_ANSWER_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.incomingCallNotificationListener.onAnswer();
    } else if (INCOMING_CALL_TERMINATE_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.incomingCallNotificationListener.onTerminate();
    }

    finish();
  }
}
