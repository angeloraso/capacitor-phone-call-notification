package ar.com.anura.plugins.phonecallnotification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class PhoneCallNotification implements IncomingCallNotificationService.CallBack, CallInProgressNotificationService.CallBack {

  private static AppCompatActivity activity;
  private static NotificationSettings mSettings;
  private static IncomingCallNotificationListener incomingCallNotificationListener;

  private static CallInProgressNotificationListener callInProgressNotificationListener;
  private static IncomingCallNotificationService incomingCallNotificationService;
  private static CallInProgressNotificationService callInProgressNotificationService;
  private static Boolean mShouldUnbindIncomingCallService = false;
  private static Boolean mShouldUnbindCallInProgressService = false;
  private static ServiceConnection mIncomingCallServiceConnection;
  private static ServiceConnection mCallInProgressServiceConnection;

  public static void initialize(final AppCompatActivity activity) {
    PhoneCallNotification.activity = activity;
    mSettings = new NotificationSettings();
  }

  @Override
  public void onIncomingCallNotificationTap() {
    if (incomingCallNotificationListener != null) {
      incomingCallNotificationListener.onTap();
      openApp("tap");
    }
  }

  @Override
  public void onDecline() {
    if (incomingCallNotificationListener != null) {
      incomingCallNotificationListener.onDecline();
    }
  }

  @Override
  public void onAnswer() {
    if (incomingCallNotificationListener != null) {
      incomingCallNotificationListener.onAnswer();
      openApp("answer");
    }
  }

  @Override
  public void onIncomingCallNotificationTerminate() {
    if (incomingCallNotificationListener != null) {
      incomingCallNotificationListener.onTerminate();
      openApp("terminate");
    }
  }

  @Override
  public void onTap() {
    if (callInProgressNotificationListener != null) {
      callInProgressNotificationListener.onTap();
      openApp("tap");
    }
  }

  @Override
  public void onHold() {
    if (callInProgressNotificationListener != null) {
      callInProgressNotificationListener.onHold();
    }
  }

  @Override
  public void onTerminate() {
    if (callInProgressNotificationListener != null) {
      callInProgressNotificationListener.onTerminate();
    }
  }

  public static void showIncomingCallNotification(final NotificationSettings settings, final IncomingCallNotificationListener listener) {
    mSettings = settings;
    incomingCallNotificationListener = listener;
    mIncomingCallServiceConnection =
      new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
          incomingCallNotificationService = ((IncomingCallNotificationService.LocalBinder) iBinder).getService();
          incomingCallNotificationService.setCallBack(new PhoneCallNotification());
          incomingCallNotificationService.setSettings(mSettings);
          incomingCallNotificationService.createNotification();
        }

        public void onServiceDisconnected(ComponentName className) {
          incomingCallNotificationService = null;
        }
      };

    Intent intent = new Intent(activity.getApplicationContext(), IncomingCallNotificationService.class);

    activity.getApplicationContext().bindService(intent, mIncomingCallServiceConnection, Context.BIND_AUTO_CREATE);
    activity.getApplicationContext().startForegroundService(intent);
    mShouldUnbindIncomingCallService = true;
  }

  public static void showCallInProgressNotification(final NotificationSettings settings, final CallInProgressNotificationListener listener) {
    mSettings = settings;
    callInProgressNotificationListener = listener;
    mCallInProgressServiceConnection =
      new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
          callInProgressNotificationService = ((CallInProgressNotificationService.LocalBinder) iBinder).getService();
          callInProgressNotificationService.setCallBack(new PhoneCallNotification());
          callInProgressNotificationService.setSettings(mSettings);
          callInProgressNotificationService.createNotification();
        }

        public void onServiceDisconnected(ComponentName className) {
          callInProgressNotificationService = null;
        }
      };

    Intent intent = new Intent(activity.getApplicationContext(), CallInProgressNotificationService.class);

    activity.getApplicationContext().bindService(intent, mCallInProgressServiceConnection, Context.BIND_AUTO_CREATE);
    activity.getApplicationContext().startForegroundService(intent);
    mShouldUnbindCallInProgressService = true;
  }

  public static void hideIncomingCall() {
    stopIncomingCallService();
  }

  public static void hideCallInProgress() {
    stopCallInProgressService();
  }

  public static void onResume() {
    stopService();
  }

  public static void onDestroy() {
    stopService();
  }

  public static boolean areNotificationsEnabled() {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
    return notificationManager.areNotificationsEnabled();
  }

  private static void stopService() {
    stopIncomingCallService();
    stopCallInProgressService();
  }

  private static void stopIncomingCallService() {
    if (mShouldUnbindIncomingCallService && mIncomingCallServiceConnection != null) {
      Intent intent = new Intent(activity.getApplicationContext(), IncomingCallNotificationService.class);
      activity.getApplicationContext().unbindService(mIncomingCallServiceConnection);
      activity.getApplicationContext().stopService(intent);
      mShouldUnbindIncomingCallService = false;
    }
  }

  private static void stopCallInProgressService() {
    if (mShouldUnbindCallInProgressService && mCallInProgressServiceConnection != null) {
      Intent intent = new Intent(activity.getApplicationContext(), CallInProgressNotificationService.class);
      activity.getApplicationContext().unbindService(mCallInProgressServiceConnection);
      activity.getApplicationContext().stopService(intent);
      mShouldUnbindCallInProgressService = false;
    }
  }

  private static void openApp(String param) {
    Intent intent = activity.getApplicationContext().getPackageManager().getLaunchIntentForPackage(activity.getApplicationContext().getPackageName());
    if (intent != null) {
      intent.putExtra("phone-call-notification", param);
      activity.getApplicationContext().startActivity(intent);
    }
  }
}
