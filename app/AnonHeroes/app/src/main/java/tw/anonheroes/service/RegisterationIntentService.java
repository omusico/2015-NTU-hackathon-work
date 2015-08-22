package tw.anonheroes.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import tw.anonheroes.api.ApiService;

/**
 * Created by ivan on 8/22/15.
 */
public class RegisterationIntentService extends IntentService {
    public static final String TAG = RegisterationIntentService.class.getSimpleName();

    public RegisterationIntentService(){
        super(TAG);
    }

    public RegisterationIntentService(String name) {
        super(name);
    }
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {

                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken("490667948417", GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(token);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    private void sendRegistrationToServer(String token){
        new ApiService().sendRegisteration(token);
    }
}
