package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Objects;

public class PushNotificationActivity extends AppCompatActivity {
  private static final String TAG = "PushNotificationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setShowWhenLocked(true);
    setTurnScreenOn(true);

    Intent intent = getIntent();
    Context context = getApplicationContext();

    Log.d(TAG, "[Push Notification] Push notification action has been received in activity");

    int notificationId = intent.getIntExtra("notificationId", -1);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.cancel(notificationId);

    String response = intent.getAction();
    String callId = intent.getStringExtra("callId");

    SharedPreferences sharedPreferences = context.getSharedPreferences("push_notification_storage", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("callId", callId);
    editor.putString("response", response);
    editor.apply();

    if (Objects.equals(response, "tap") || Objects.equals(response, "answer")) {
      Class<? extends AppCompatActivity> mainActivity = getMainActivityClass(context);
      Intent mainIntent = new Intent(context, mainActivity);
      mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      context.startActivity(mainIntent);
    }

    finish();
  }

  private Class<? extends AppCompatActivity> getMainActivityClass(Context context) {
    try {
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
