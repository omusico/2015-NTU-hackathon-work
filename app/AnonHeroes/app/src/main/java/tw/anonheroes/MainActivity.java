package tw.anonheroes;


import tw.anonheroes.service.RegisterationIntentService;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.THLight.USBeacon.App.Lib.USBeaconData;
import com.THLight.USBeacon.App.Lib.USBeaconList;
import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
import com.THLight.USBeacon.App.Lib.iBeaconData;
import com.THLight.USBeacon.App.Lib.iBeaconScanManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import tw.anonheroes.api.ApiService;
import tw.anonheroes.model.pojo.ScanediBeacon;

public class MainActivity extends AppCompatActivity implements iBeaconScanManager.OniBeaconScan, USBeaconConnection.OnResponse  {


    BluetoothAdapter mBLEAdapter= BluetoothAdapter.getDefaultAdapter();

    /** this UUID is generate by Server while register a new account. */
    final UUID QUERY_UUID		= UUID.fromString("2FB0A9F1-E4AF-4334-94CD-0AA798165069");

    /** server http api url. */
    final String HTTP_API		= "http://www.usbeacon.com.tw/api/func";

    /** scaner for scanning iBeacon around. */
    iBeaconScanManager miScaner	= null;

    /** USBeacon server. */
    USBeaconConnection mBServer	= new USBeaconConnection();

    final int REQ_ENABLE_BT		= 2000;
    final int REQ_ENABLE_WIFI	= 2001;

    final int MSG_SCAN_IBEACON			= 1000;
    final int MSG_UPDATE_BEACON_LIST	= 1001;
    final int MSG_START_SCAN_BEACON		= 2000;
    final int MSG_STOP_SCAN_BEACON		= 2001;
    final int MSG_SERVER_RESPONSE		= 3000;

    final int TIME_BEACON_TIMEOUT		= 10000;

    static String STORE_PATH	= Environment.getExternalStorageDirectory().toString()+ "/USBeaconSample/";

    List<ScanediBeacon> miBeacons	= new ArrayList<ScanediBeacon>();


    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** create instance of iBeaconScanManager. */
        miScaner = new iBeaconScanManager(this, this);

        if(!mBLEAdapter.isEnabled())
        {
            Intent intent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_ENABLE_BT);
        }
        else
        {
            Message msg= Message.obtain(mHandler, MSG_SCAN_IBEACON, 1000, 1100);
            msg.sendToTarget();
        }

        /** create store folder. */
        File file= new File(STORE_PATH);
        if(!file.exists())
        {
            if(!file.mkdirs())
            {
                Toast.makeText(this, "Create folder(" + STORE_PATH + ") failed.", Toast.LENGTH_SHORT).show();
            }
        }

        /** check network is available or not. */
        ConnectivityManager cm	= (ConnectivityManager)getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        if(null != cm)
        {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if(null == ni || (!ni.isConnected()))
            {
                //TODO: connect error handler
            }
            else
            {
                NetworkInfo niMobile= cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if(null != niMobile)
                {
                    boolean is3g	= niMobile.isConnectedOrConnecting();

                    USBeaconServerInfo info= new USBeaconServerInfo();

                    info.serverUrl		= HTTP_API;
                    info.queryUuid		= QUERY_UUID;
                    info.downloadPath	= STORE_PATH;

                    mBServer.setServerInfo(info, MainActivity.this);
                    mBServer.checkForUpdates();
                }
            }
        }
        else
        {
            //TODO:  network is not available handle
        }
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegisterationIntentService.class);
            startService(intent);
        }



    }

    @Override
    public void onResponse(int msg) {
        mHandler.obtainMessage(MSG_SERVER_RESPONSE, msg, 0).sendToTarget();
    }

    @Override
    public void onScaned(final iBeaconData iBeacon) {
        runOnUiThread(new Runnable() {
            public void run() {
                addOrUpdateiBeacon(iBeacon);
                //Log.d("Beacon Count:", Integer.toString(miBeacons.size()));

            }
        });
    }

    public void addOrUpdateiBeacon(iBeaconData iBeacon)
    {

        long currTime= System.currentTimeMillis();

        ScanediBeacon beacon= null;

        for(ScanediBeacon b : miBeacons)
        {
            if(b.equals(iBeacon, false))
            {
                beacon= b;
                break;
            }
        }

        if(null == beacon)
        {
            beacon= ScanediBeacon.copyOf(iBeacon);
            miBeacons.add(beacon);
        }
        else
        {
            beacon.rssi= iBeacon.rssi;
        }

        beacon.lastUpdate= currTime;
    }

    public void verifyiBeacons()
    {
        {
            long currTime	= System.currentTimeMillis();

            int len= miBeacons.size();
            ScanediBeacon beacon= null;

            for(int i= len- 1; 0 <= i; i--)
            {
                beacon= miBeacons.get(i);

                if(null != beacon && TIME_BEACON_TIMEOUT < (currTime- beacon.lastUpdate))
                {
                    miBeacons.remove(i);
                }
            }
        }
    }

    public void getHelp(String result)
    {
        int len = miBeacons.size();
        ScanediBeacon beacon= null;
        USBeaconList BList= mBServer.getUSBeaconList();

        //SharedPreferences setting = getSharedPreferences("Preference", 0);

        int minMajor = 0, minMinor = 0;
        double minDistance = 0.0;
        for(int i= len- 1; 0 <= i; i--)
        {
            beacon= miBeacons.get(i);
            if(minDistance < 0.00000000001 || minDistance > beacon.calDistance()){
                minDistance = beacon.calDistance();
                minMajor = beacon.major;
                minMinor = beacon.minor;
            }



        }

        //String tempString ="";
        for(USBeaconData data : BList.getList())
        {
            //test
            if(minMajor == data.major && minMinor == data.minor){
                new ApiService().sendHelp(data.major, data.minor, result);
            }
            //tempString = "major:" + data.major + ",minor:"+data.minor+",url:"+data.DistData.get("Near").strImageUrl;
        }
        //setting.edit().putString();
    }

    public USBeaconList getBeaconList(){
        return mBServer.getUSBeaconList();
    }

    Handler mHandler= new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case MSG_SCAN_IBEACON:
                {
                    int timeForScaning		= msg.arg1;
                    int nextTimeStartScan	= msg.arg2;

                    miScaner.startScaniBeacon(timeForScaning);
                    this.sendMessageDelayed(Message.obtain(msg), nextTimeStartScan);
                }
                break;

                case MSG_UPDATE_BEACON_LIST:
                    verifyiBeacons();
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);
                    break;

                case MSG_SERVER_RESPONSE:
                    switch(msg.arg1)
                    {
                        case USBeaconConnection.MSG_NETWORK_NOT_AVAILABLE:
                            break;

                        case USBeaconConnection.MSG_HAS_UPDATE:
                            mBServer.downloadBeaconListFile();
                            //Toast.makeText(BeaconMainActivity.this, "HAS_UPDATE.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_HAS_NO_UPDATE:
                            //Toast.makeText(BeaconMainActivity.this, "No new BeaconList.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FINISHED:
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FAILED:
                            Toast.makeText(MainActivity.this, "Download file failed!", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FINISHED:
                        {
                            USBeaconList BList= mBServer.getUSBeaconList();

                            if(null == BList)
                            {
                                Toast.makeText(MainActivity.this, "Data Updated failed.", Toast.LENGTH_SHORT).show();
                                //Log.d("debug", "update failed.");
                            }
                            else if(BList.getList().isEmpty())
                            {
                                Toast.makeText(MainActivity.this, "Data Updated but empty.", Toast.LENGTH_SHORT).show();
                                //Log.d("debug", "this account doesn't contain any devices.");
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Data Updated("+ BList.getList().size()+ ")", Toast.LENGTH_SHORT).show();

                                for(USBeaconData data : BList.getList())
                                {
                                    //Log.d("debug", "Name("+ data.name+ "), Ver("+ data.major+ "."+ data.minor+ ")");
                                }
                            }
                        }
                        break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FAILED:
                            Toast.makeText(MainActivity.this, "UPDATE_FAILED!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
