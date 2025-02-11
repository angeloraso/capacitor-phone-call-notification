package ar.com.anura.plugins.phonecallnotification;

import android.os.Handler;
import android.os.Looper;

/**
 * Utility class to be able to run code on the app main thread.
 */
public final class AndroidDispatcher {
  private static final Handler sHandler = new Handler(Looper.getMainLooper());

  private AndroidDispatcher() {
  }

  public static void dispatchOnUIThread(Runnable r) {
    sHandler.post(r);
  }

  public static void dispatchOnUIThreadAfter(Runnable r, long after) {
    sHandler.postDelayed(r, after);
  }

  public static void removeFromUIThreadDispatcher(Runnable r) {
    sHandler.removeCallbacks(r);
  }
}
