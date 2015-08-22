package tw.anonheroes.service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import de.greenrobot.event.EventBus;
import tw.anonheroes.event.GcmReceiveEvent;

/**
 * Created by ivan on 8/22/15.
 */

public class GcmListenService extends GcmListenerService{

    public static final String TAG = GcmListenService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("type");
        Log.i(TAG, from + ": " + message);

        SharedPreferences setting = getSharedPreferences("Preference", 1);
        String result = setting.getString("PhotoInfoString", "default value");
        // TODO
        EventBus.getDefault().post(new GcmReceiveEvent(result));

        super.onMessageReceived(from, data);
    }

}
