package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_HOLD_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_MUTE_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_SWITCH_SESSION_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_TERMINATE_ACTION;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class CallInProgressNotificationActivity extends AppCompatActivity {

  private static final String TAG = "CallInProgressNotificationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "Phone call in progress notification activity");

    Intent intent = getIntent();

    String response = getResponse(intent.getAction());
    if (response != null) {
      PhoneCallNotification.dispatchResponse(getApplicationContext(), response);
      if (CALL_IN_PROGRESS_TERMINATE_ACTION.equals(intent.getAction())) {
        CallInProgressNotificationService.stopService(getApplicationContext());
      }
    }

    openMainActivity();
    finish();
  }

  private String getResponse(String action) {
    if (CALL_IN_PROGRESS_TAP_ACTION.equals(action)) return "tap";
    if (CALL_IN_PROGRESS_HOLD_ACTION.equals(action)) return "hold";
    if (CALL_IN_PROGRESS_MUTE_ACTION.equals(action)) return "mute";
    if (CALL_IN_PROGRESS_SWITCH_SESSION_ACTION.equals(action)) return "switchSession";
    if (CALL_IN_PROGRESS_TERMINATE_ACTION.equals(action)) return "terminate";
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
