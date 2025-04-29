package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class IncomingCallFullNotificationActivity extends AppCompatActivity {

  private static final String TAG = "IncomingCallFullNotificationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "Phone call full notification activity");

    setShowWhenLocked(true);
    setTurnScreenOn(true);

    Context context = getApplicationContext();
    Class<? extends AppCompatActivity> mainActivity = getMainActivityClass(context);
    Intent mainIntent = new Intent(context, mainActivity);
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    context.startActivity(mainIntent);

    Log.d(TAG, "Finish activity");
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
