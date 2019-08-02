package com.lab.uqac.emotibit.application.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    public void enableHotspot(View view){

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLinkEmotiBitReceptor.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLinkEmotiBitReceptor = new LinkEmotiBitReceptor(mConnection, mEmotiBitButton);
        mLinkEmotiBitReceptor.start();
        Toast.makeText(this, "EmotiBit search in progress", Toast.LENGTH_LONG).show();
    }


}

