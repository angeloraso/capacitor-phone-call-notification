package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.PhoneCallNotification.CALL_IN_PROGRESS_HOLD_ACTION;
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

    if (PhoneCallNotification.callInProgressNotificationListener != null) {
      Log.d(TAG, "callInProgressNotificationListener is not null");
      if (CALL_IN_PROGRESS_TAP_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.callInProgressNotificationListener.onTap();
      } else if (CALL_IN_PROGRESS_HOLD_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.callInProgressNotificationListener.onHold();
      } else if (CALL_IN_PROGRESS_TERMINATE_ACTION.equals(intent.getAction())) {
        PhoneCallNotification.callInProgressNotificationListener.onTerminate();
      }
    } else {
      Log.d(TAG, "callInProgressNotificationListener is null");
      Context context = getApplicationContext();
      Class<? extends AppCompatActivity> mainActivity = getMainActivityClass(context);
      Intent mainIntent = new Intent(context, mainActivity);
      mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      context.startActivity(mainIntent);
    }

    finish();
  }

  private Class<? extends AppCompatActivity> getMainActivityClass(Context context) {
    try {
      Log.d(TAG, "Open main activity");
      String packageName = context.getPackageName();
      Class<?> mainActivityClass = Class.forName(packageName + ".MainActivity");
      if (AppCompatActivity.class.isAssignableFrom(mainActivityClass)) {
        return mainActivityClass.asSubclass(AppCompatActivity.class);
      } else {
        throw new RuntimeException("MainActivity does not extend AppCompatActivity.");
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Unable to resolve MainActivity class.");
    }
  }
}
