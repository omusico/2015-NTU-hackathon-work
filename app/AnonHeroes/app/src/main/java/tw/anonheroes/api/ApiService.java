package tw.anonheroes.api;

import android.util.Log;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.POST;
import retrofit.http.Query;
import tw.anonheroes.model.pojo.Result;

/**
 * Created by ivan on 8/22/15.
 */
public class ApiService {
    public static final String API_URL = "http://192.168.23.18:9000";
    public static final String TAG = ApiService.class.getSimpleName();
    private Api mApi = null;

    public ApiService(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        mApi = restAdapter.create(Api.class);
    }

    public void getHelp(int major, int mirror, String helpString){
        mApi.getHelp(major, mirror, helpString, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                Log.i(TAG, "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "fail! " + error.getMessage());
            }
        });
    }

    public interface Api{
        @POST("/rest/help")
        void getHelp(@Query("major") int beaconMajor, @Query("mirror") int beaconMinor, @Query("help_string") String help, Callback<Result> callback);
    }
}
