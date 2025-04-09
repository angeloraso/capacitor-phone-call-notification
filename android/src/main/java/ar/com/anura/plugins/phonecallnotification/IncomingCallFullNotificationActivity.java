package ar.com.anura.plugins.phonecallnotification;

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

        Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
        if (intent != null) {
            Log.d(TAG, "Open main activity");
            getApplicationContext().startActivity(intent);
        }

        Log.d(TAG, "Finish activity");
        finish();
    }
}
