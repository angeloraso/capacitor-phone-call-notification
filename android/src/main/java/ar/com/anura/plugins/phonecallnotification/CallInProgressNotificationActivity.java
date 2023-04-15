package ar.com.anura.plugins.phonecallnotification;

import static ar.com.anura.plugins.phonecallnotification.CallInProgressNotificationService.HOLD_ACTION;
import static ar.com.anura.plugins.phonecallnotification.CallInProgressNotificationService.TAP_ACTION;
import static ar.com.anura.plugins.phonecallnotification.CallInProgressNotificationService.TERMINATE_ACTION;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CallInProgressNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (CallInProgressNotificationService.callBack == null) {
            return;
        }

        if (TAP_ACTION.equals(intent.getAction())) {
          CallInProgressNotificationService.callBack.onTap();
        } else if (HOLD_ACTION.equals(intent.getAction())) {
          CallInProgressNotificationService.callBack.onHold();
        } else if (TERMINATE_ACTION.equals(intent.getAction())) {
          CallInProgressNotificationService.callBack.onTerminate();
        }

        finish();
    }
}
