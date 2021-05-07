package com.lab.uqac.emotibit.application.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lab.uqac.emotibit.application.launcher.Network.Connection;
import com.lab.uqac.emotibit.application.launcher.Network.Link.LinkEmotiBitReceptor;
import com.lab.uqac.emotibit.application.launcher.Network.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GUI.EmotiBitButton;


public class MainActivity extends AppCompatActivity {

    private Button mButtonExit;


    private LinearLayout mVerticalLayout;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Connection mConnection;
    private EmotiBitButton mEmotiBitButton;
    private Button mButtonHotspot;
    private NetworkUtils mNetworkUtils;
    private LinkEmotiBitReceptor mLinkEmotiBitReceptor;



    private int mIndex = 0;
    private HashMap<InetAddress, View> mMapButton;
    private LinearLayout.LayoutParams mParams;
    private String mTextButton;
    private List<InetAddress> mSavedList;


    /** Our arbitrary user code for permissions */
    public static final int MULTIPLE_PERMISSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checking for wifi connection
//        Context context = this;
//        ConnectivityManager cm =
//                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
//
//        if(!(isConnected && isWiFi)){
//            Toast.makeText(this, "WiFi not connected.Please connect to Wifi or enable HotSpot", Toast.LENGTH_LONG).show();
//
//        }
//        while(!(isConnected && isWiFi)){
//            super.onResume();
//            Toast.makeText(this, "Connect to WiFi", Toast.LENGTH_LONG).show();
//
//        }
        // end modifications


        //set up permissions



        if (    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_MULTICAST_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_WIFI_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CHANGE_WIFI_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CHANGE_WIFI_MULTICAST_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CHANGE_NETWORK_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
                                Manifest.permission.CHANGE_NETWORK_STATE},MULTIPLE_PERMISSIONS);

            }
        } else {

            Log.d("EMOTIBIT MAIN", "Permissions OK");
            init();
        }




    }

    public void enableHotspot(View view){

    }

    /**
     * Once our permissions are verified and "asked for" we then call this function to do the initialization
     */
    private void init() {

        mButtonExit = (Button) findViewById(R.id.button_exit);
        mVerticalLayout = (LinearLayout) findViewById(R.id.row_main);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });

        mMapButton = new HashMap<InetAddress, View>();
        mSavedList = new ArrayList<InetAddress>();

        mButtonHotspot = findViewById(R.id.button_connect);

        int maxDevice = Integer.valueOf(getString(R.string.max_devices));

        mEmotiBitButton = new EmotiBitButton(this, maxDevice);

        // we have to allow packets in from multicast for this to work
        setMulticastReceive();

        mNetworkUtils = new NetworkUtils(this);

        int port = Integer.valueOf(getString(R.string.port_number));

        try {
            InetAddress broadcastAddress = mNetworkUtils.extractBroadcastAddress();

            if(broadcastAddress != null)
                mConnection = new Connection(port, broadcastAddress, maxDevice);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    private WifiManager.MulticastLock lock = null;

    private void setMulticastReceive() {

        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService( Context.WIFI_SERVICE );
        if(wifi != null){
            WifiManager.MulticastLock lock = wifi.createMulticastLock("Log_Tag");
            lock.acquire();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLinkEmotiBitReceptor.stop();
        if (lock != null) {
            lock.release();
            lock = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMulticastReceive();
        mLinkEmotiBitReceptor = new LinkEmotiBitReceptor(mConnection, mEmotiBitButton);
        mLinkEmotiBitReceptor.start();
        Toast.makeText(this, "EmotiBit search in progress", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lock != null) {
            lock.release();
            lock = null;
        }

    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    init();
                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }
}

