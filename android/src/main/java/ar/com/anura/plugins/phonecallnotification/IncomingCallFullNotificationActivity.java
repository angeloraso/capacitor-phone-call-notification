package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingCallFullNotificationActivity extends AppCompatActivity {

  private static final String TAG = "IncomingCallFullNotificationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "Phone call full notification activity");

    setShowWhenLocked(true);
    setTurnScreenOn(true);

    Context context = getApplicationContext();
    Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    if (mainIntent != null) {
      mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      context.startActivity(mainIntent);
    } else {
      Log.e(TAG, "Unable to resolve the host application's launcher activity");
    }

    Log.d(TAG, "Finish activity");
    finish();
  }
}
