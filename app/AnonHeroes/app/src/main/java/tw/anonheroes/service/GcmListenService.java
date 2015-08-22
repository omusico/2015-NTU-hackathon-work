package tw.anonheroes.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by ivan on 8/22/15.
 */
public class GcmListenService extends GcmListenerService {
    public static final String TAG = GcmListenService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i(TAG, from + ": " + data.getString("data"));
        super.onMessageReceived(from, data);
    }
}
