package tw.anonheroes.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.UUID;

import tw.anonheroes.MainActivity;
import tw.anonheroes.R;

import static com.google.android.gms.internal.zzhl.runOnUiThread;

/**
 * Created by ivan on 8/22/15.
 */
public class GcmListenService extends GcmListenerService implements USBeaconConnection.OnResponse {

    public static final String TAG = GcmListenService.class.getSimpleName();


    @Override
    public void onMessageReceived(String from, Bundle data) {

        // TODO: GET NOTIFICATION
        String message = data.getString("type");
        Log.i(TAG, from + ": " + message);

        SharedPreferences setting = getSharedPreferences("Preference", 1);
        String result = setting.getString("PhotoInfoString", "default value");

        //final Dialog dialog = new Dialog(this);
        //runOnUiThread(new Runnable() {
        //    public void run() {
        //        dialog.show();
        //    }
        //});



        super.onMessageReceived(from, data);
    }

    @Override
    public void onResponse(int i) {

    }



}
