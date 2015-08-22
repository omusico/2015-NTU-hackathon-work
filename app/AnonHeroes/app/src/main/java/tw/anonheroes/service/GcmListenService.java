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
        String message = data.getString("type").replace("Some(","").replace("","");
        Log.i(TAG, from + ": " + message);

        SharedPreferences setting = getSharedPreferences("Preference", 1);
        String result = setting.getString("PhotoInfoString", "default value");

        GcmReceiveEvent ge = new GcmReceiveEvent(result);
        if(message.indexOf("110") >= 0)  ge.setIs110(true) ; else ge.setIs110(false);
        if(message.indexOf("113") >= 0)  ge.setIs113(true) ; else ge.setIs113(false);
        if(message.indexOf("119") >= 0)  ge.setIs119(true) ; else ge.setIs119(false);

        if(result.indexOf('@') >=0){
            String[] mainArray =  result.split("@");
            String[] detailArray = mainArray[0].split(",");

            ge.setUrl(detailArray[2]);
        }

        // TODO
        EventBus.getDefault().post(ge);

        super.onMessageReceived(from, data);
    }

}
