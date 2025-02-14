package ar.com.anura.plugins.phonecallnotification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PushNotificationFullActivity extends AppCompatActivity {
  private static final String TAG = "FullPushNotificationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "[Push Notification] Push notification full activity");

    setShowWhenLocked(true);
    setTurnScreenOn(true);

    Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
    if (intent != null) {
      Log.d(TAG, "[Push Notification] Open main activity");
      getApplicationContext().startActivity(intent);
    }

    Log.d(TAG, "[Push Notification] Finish");
    finish();
  }
}
