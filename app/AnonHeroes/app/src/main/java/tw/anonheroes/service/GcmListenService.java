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
        String message = data.getString("type").replace("Some(","").replace("\\)","");
        Log.i(TAG, from + ": " + message);

        SharedPreferences setting = getSharedPreferences("Preference", 1);
        String result = setting.getString("PhotoInfoString", "default value");



        GcmReceiveEvent ge = new GcmReceiveEvent("");
        if(message.indexOf("110") >= 0)  {ge.setIs110(true) ; ge.setResult("有人需要被保護\n地點：台大體育館"); }

        else if(message.indexOf("119") >= 0)  {ge.setIs119(true) ; ge.setResult("有人需要醫療協助\n地點：台大體育館") ;}
        else   {ge.setIs113(true) ; ge.setResult("有人需要協助\n地點：台大體育館") ;}




        if(result.indexOf('@') >=0){
            String[] mainArray =  result.split("@");
            String[] detailArray = mainArray[0].split(",");

            ge.setUrl(detailArray[2].replace("url:",""));
        }

        // TODO
        EventBus.getDefault().post(ge);

        super.onMessageReceived(from, data);
    }

}
