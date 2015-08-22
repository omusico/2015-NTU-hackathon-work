package tw.anonheroes.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.UUID;

import tw.anonheroes.MainActivity;

/**
 * Created by ivan on 8/22/15.
 */
public class GcmListenService extends GcmListenerService implements USBeaconConnection.OnResponse {

    public static final String TAG = GcmListenService.class.getSimpleName();

    final UUID QUERY_UUID		= UUID.fromString("2FB0A9F1-E4AF-4334-94CD-0AA798165069");
    final String HTTP_API		= "http://www.usbeacon.com.tw/api/func";
    final int MSG_UPDATE_BEACON_LIST	= 1001;


    @Override
    public void onMessageReceived(String from, Bundle data) {

        // TODO: GET NOTIFICATION
        Log.i(TAG, from + ": " + data.getString("data"));



        super.onMessageReceived(from, data);
    }

    @Override
    public void onResponse(int i) {

    }


}
