package ar.com.anura.plugins.phonecallnotification;

import android.content.Context;
import android.content.SharedPreferences;
import com.getcapacitor.JSObject;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

final class NotificationSettingsStore {

  static final String INCOMING_KEY = "incoming";
  static final String IN_PROGRESS_KEY = "inProgress";

  private static final String PREFERENCES_NAME = "phone-call-notification-settings";
  private static final String INCOMING_PREFERENCES_KEY = "incoming-settings";
  private static final String IN_PROGRESS_PREFERENCES_KEY = "in-progress-settings";

  private final SharedPreferences preferences;

  NotificationSettingsStore(Context context) {
    preferences = context.getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
  }

  JSObject mergeIncoming(JSObject overrides) {
    return merge(read(INCOMING_PREFERENCES_KEY), overrides);
  }

  JSObject mergeInProgress(JSObject overrides) {
    return merge(read(IN_PROGRESS_PREFERENCES_KEY), overrides);
  }

  void update(JSObject incoming, JSObject inProgress) {
    SharedPreferences.Editor editor = preferences.edit();
    if (incoming != null) {
      editor.putString(INCOMING_PREFERENCES_KEY, mergeIncoming(incoming).toString());
    }
    if (inProgress != null) {
      editor.putString(IN_PROGRESS_PREFERENCES_KEY, mergeInProgress(inProgress).toString());
    }
    editor.apply();
  }

  private JSObject read(String key) {
    String value = preferences.getString(key, null);
    if (value == null) {
      return new JSObject();
    }
    try {
      return new JSObject(value);
    } catch (JSONException exception) {
      preferences.edit().remove(key).apply();
      return new JSObject();
    }
  }

  private JSObject merge(JSObject base, JSObject overrides) {
    JSObject result = new JSObject();
    copy(base, result);
    copy(overrides, result);
    return result;
  }

  private void copy(JSONObject source, JSObject target) {
    if (source == null) {
      return;
    }
    Iterator<String> keys = source.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      target.put(key, source.opt(key));
    }
  }
}
