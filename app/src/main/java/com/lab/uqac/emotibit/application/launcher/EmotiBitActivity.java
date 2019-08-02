package com.lab.uqac.emotibit.application.launcher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.lab.uqac.emotibit.application.launcher.Datas.ExtractedDatas;
import com.lab.uqac.emotibit.application.launcher.Datas.ExtractionDatas;
import com.lab.uqac.emotibit.application.launcher.Datas.MessageGenerator;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;
import com.lab.uqac.emotibit.application.launcher.Drawing.PlotDatas;
import com.lab.uqac.emotibit.application.launcher.Network.Connection;
import com.lab.uqac.emotibit.application.launcher.Network.ActionSender;
import com.lab.uqac.emotibit.application.launcher.Network.DatasReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

import GUI.UpdateUI;
import listener.OnSwipeTouchListener;

public class EmotiBitActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, View.OnTouchListener {

    private Context mContext;
    private TextView mTextViewIP;
    private TextView mTextViewGPS;
    private TextView mTextViewHib;
    private Button mButtonRecord;
    private Button mButtonGPS;
    private Button mButtonHibernate;
    private Button mButtonSendNote;
    private GoogleMap mMap;
    private EditText mEditText;
    private ScrollView mScrollView;
    private int mColor;
    private UpdateUI mUpdateUI;
    private Connection mConnection = null;
    private ActionSender mActionSender;
    private DatasReceiver mDatasReceiver;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final String LOG_TAG = EmotiBitActivity.class.getSimpleName();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotibit);
        Intent intent = getIntent();
//        String title = intent.getStringExtra("selected");
        int port = Integer.valueOf(getString(R.string.port_number));
        InetAddress address = (InetAddress) intent.getSerializableExtra("address");

//        getSupportActionBar().setTitle(title);
        mContext = getApplicationContext();
        mTextViewGPS = (TextView) findViewById(R.id.text_gps);
     //   mTextViewIP = (TextView) findViewById(R.id.text_ip);
        mTextViewHib = (TextView) findViewById(R.id.text_hibernate);
        mTextViewHib.setText("Hibernate: "+ "OFF");
        mTextViewGPS.setText("GPS: " + "OFF");
        mButtonRecord = (Button) findViewById(R.id.button_record);
        ColorDrawable viewColor = (ColorDrawable) mButtonRecord.getBackground();
        mColor = viewColor.getColor();

        mButtonGPS = (Button) findViewById(R.id.button_gps);
        mButtonHibernate = (Button) findViewById(R.id.button_hibernate);
        mButtonSendNote = (Button) findViewById(R.id.button_send);
        mButtonSendNote.setOnClickListener(this);
        mButtonRecord.setOnClickListener(this);
        mButtonGPS.setOnClickListener(this);
        mButtonHibernate.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.editText_note);
        mEditText.setOnTouchListener(this);
        mEditText.setFocusable(false);
        mScrollView = findViewById(R.id.scroll_v);
        mScrollView.setOnTouchListener(this);

        //MAP
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int index, KeyEvent keyEvent) {

                if (index == EditorInfo.IME_ACTION_DONE) {
                    //  editText.clearFocus();
                    //editText.setFocusable(false);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        mEditText.setTextColor(Color.GRAY);
        mEditText.setBackgroundColor(Color.YELLOW);

        mUpdateUI = new UpdateUI(this, mColor);

        mUpdateUI.setupSpinner();
        mUpdateUI.initChartParameters();

        mConnection = new Connection(null, address, port);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatasReceiver = new DatasReceiver(mConnection, mUpdateUI);
        mDatasReceiver.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatasReceiver.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if(view == mEditText ){
            mEditText.setFocusableInTouchMode(true);
            mEditText.setFocusable(true);
            mEditText.setBackgroundColor(Color.TRANSPARENT);
            mEditText.setText("");
            mEditText.setTextColor(Color.BLACK);
            return false;
        }else if(view == mScrollView && (event.getX()< mEditText.getX() || event.getX()> mEditText.getMeasuredWidth())
                && (event.getY()< mEditText.getY()  || event.getY()> mEditText.getMeasuredHeight())){

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            mEditText.setFocusableInTouchMode(false);
            mEditText.setFocusable(false);
            if(mEditText.getText().toString().trim().compareTo("") == 0)
            {
                mEditText.setBackgroundColor(Color.YELLOW);
                mEditText.setText("Add a note");
                mEditText.setTextColor(Color.GRAY);
            }
            return false;
        }
        return false;
    }


    @Override
    public void onClick(View view) {

        if( view == mButtonRecord)
        {
            if(!mUpdateUI.ismIsRecord())
            {
                MessageGenerator messageGenerator = new MessageGenerator(TypesDatas.RB,
                        1, 1, 100, "", false);

                ActionSender actionSender = new ActionSender(messageGenerator, mConnection);
                actionSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }else{

                MessageGenerator messageGenerator = new MessageGenerator(TypesDatas.RE,
                        1, 1, 100, "", false);

                ActionSender actionSender = new ActionSender(messageGenerator, mConnection);
                actionSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        else if ( view == mButtonHibernate)
        {
            showConfirmationMessage();
        }
        else if( view == mButtonGPS)
        {
            if(!mUpdateUI.ismIsGPS())
            {
                mUpdateUI.updateGpsObject();
                statusGPSCheck();
            }else{
                mUpdateUI.updateGpsObject();
            }
        }
        else if( view == mButtonSendNote)
        {
            String note = mEditText.getText().toString();
            if(!note.isEmpty()) {
                MessageGenerator messageGenerator = new MessageGenerator(TypesDatas.UN,
                        1, 1, 100, note, false);

                ActionSender actionSender = new ActionSender(messageGenerator, mConnection);
                actionSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
                Toast.makeText(this, "You need to insert a note before sending !",
                        Toast.LENGTH_LONG).show();
        }
    }

    private void showConfirmationMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm action");
        builder.setMessage("Do you really want to send this action ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(!mUpdateUI.ismIsHibernate())
                {
                    MessageGenerator messageGenerator = new MessageGenerator(TypesDatas.MH,
                            1, 1, 100, "", false);

                    ActionSender actionSender = new ActionSender(messageGenerator, mConnection);
                    actionSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                  /*  mButtonHibernate.setText("Hibernate");
                    mImViewLogo.setImageResource(R.drawable.logo_wake_72);
                    mIsHibernate = false;
                    mTextViewHib.setText("Hibernate: " + "OFF");*/
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);

        askForCurrentLocation();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
    }

    private void askForCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            //statusGPSCheck();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    askForCurrentLocation();

                } else {

                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng coord = new LatLng(48.351633, -71.138242);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
    }



    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };


    public void statusGPSCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        mTextViewGPS.setText("GPS: " + "ON");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        mTextViewGPS.setText("GPS: " + "OFF");
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
