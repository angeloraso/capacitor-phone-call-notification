package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_ANSWER_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_DECLINE_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.INCOMING_CALL_TERMINATE_ACTION;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingCallNotificationActivity extends AppCompatActivity {

  private static final String TAG = "IncomingCallNotificationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "Phone call notification activity");

    Intent intent = getIntent();

    String response = getResponse(intent.getAction());
    if (response != null) {
      PhoneCallNotification.dispatchResponse(getApplicationContext(), response);
      if (!INCOMING_CALL_TAP_ACTION.equals(intent.getAction())) {
        IncomingCallNotificationService.stopService(getApplicationContext());
      }
    }

    openMainActivity();
    finish();
  }

  private String getResponse(String action) {
    if (INCOMING_CALL_TAP_ACTION.equals(action)) return "tap";
    if (INCOMING_CALL_DECLINE_ACTION.equals(action)) return "decline";
    if (INCOMING_CALL_ANSWER_ACTION.equals(action)) return "answer";
    if (INCOMING_CALL_TERMINATE_ACTION.equals(action)) return "terminate";
    return null;
  }

  private void openMainActivity() {
    Context context = getApplicationContext();
    Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    if (mainIntent != null) {
      mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      context.startActivity(mainIntent);
    } else {
      Log.e(TAG, "Unable to resolve the host application's launcher activity");
    }
  }
}
