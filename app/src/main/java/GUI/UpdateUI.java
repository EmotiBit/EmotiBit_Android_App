package GUI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;
import com.lab.uqac.emotibit.application.launcher.Drawing.PlotDatas;
import com.lab.uqac.emotibit.application.launcher.R;

import java.util.HashMap;

import listener.OnSwipeTouchListener;

public class UpdateUI {

    private Activity mActivity;
    private int mColor;
    private boolean mIsRecord = false;
    private boolean mIsHibernate = false;
    private boolean mIsGPS = false;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private LineGraphSeries<DataPoint> mSeries3;
    private PlotDatas mPlotDatas;
    private GraphView mGraphView;
    private Spinner mSpinner;
    private int mSelectedGraphPosition = 0;
    private ArrayAdapter<CharSequence> mAdapter;
    private HashMap<String, TypesDatas> mMapGraphSelector;
    private int mClippingCount = 0;
    private int mOverflowCount = 0;
    private long mStartTime;

    public UpdateUI(Activity activity, int color){

        mActivity = activity;
        mColor = color;
        mStartTime = System.currentTimeMillis();
    }

    public void initChartParameters(){

        mMapGraphSelector = TypesDatas.mapString();

        mGraphView = (GraphView) mActivity.findViewById(R.id.graph);

        /*Signals*/
        mSeries1 = new LineGraphSeries<>(new DataPoint[] {});

        mSeries2 = new LineGraphSeries<>(new DataPoint[] {});

        mSeries3 = new LineGraphSeries<>(new DataPoint[] {});

        mGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        mGraphView.setOnTouchListener(new OnSwipeTouchListener(mActivity){

            @Override
            public void onSwipeLeft() {
                if(mSelectedGraphPosition > 0)
                    mSelectedGraphPosition--;

                mSpinner.setSelection(mSelectedGraphPosition);
            }

            @Override
            public void onSwipeRight() {
                if(mSelectedGraphPosition < mAdapter.getCount() - 1)
                    mSelectedGraphPosition++;
                mSpinner.setSelection(mSelectedGraphPosition);
            }
        });


        mGraphView.addSeries(mSeries1);
        mGraphView.addSeries(mSeries2);
        mGraphView.addSeries(mSeries3);

        mSeries1.setColor(Color.RED);
        mSeries2.setColor(Color.BLUE);
        mSeries3.setColor(Color.BLACK);

        mPlotDatas = new PlotDatas(mGraphView, mSeries1, mSeries2, mSeries3);
    }

    public void setupSpinner(){

        mSpinner = mActivity.findViewById(R.id.spinner);

        mAdapter = ArrayAdapter.createFromResource(mActivity,
                R.array.graph_array, R.layout.support_simple_spinner_dropdown_item);

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectedGraphPosition = position;

                mGraphView.removeAllSeries();
                mGraphView.addSeries(mSeries1);
                mGraphView.addSeries(mSeries2);
                mGraphView.addSeries(mSeries3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinner.setSelection(TypesDatas.PPGGRN.getmSelectedGraph());
    }


    public void updateButtonRecord(){
        ImageView imageView = mActivity.findViewById(R.id.imview_rec);
        Button    button    = mActivity.findViewById(R.id.button_record);

        if(!mIsRecord) {
            imageView.setImageResource(R.drawable.rec_64);
            imageView.setVisibility(View.VISIBLE);
            button.setText("Stop Recording");
            button.setBackgroundColor(Color.RED);
            mIsRecord = true;
        }else{
            imageView.setImageResource(R.drawable.rec_64);
            imageView.setVisibility(View.INVISIBLE);
            button.setText("Record Datas");
            button.setBackgroundColor(mColor);
            mIsRecord = false;
        }

    }

    public void updateHibernateState(){
        ImageView   imageView   = mActivity.findViewById(R.id.imview_log);
        Button      button      = mActivity.findViewById(R.id.button_hibernate);
        TextView    textView    = mActivity.findViewById(R.id.text_hibernate);

        if(!mIsHibernate)
        {
            button.setText("Wake");
            imageView.setImageResource(R.drawable.logo_hib_72);
            textView.setText("Hibernate: " + "ON");
        }else{
            button.setText("Hibernate");
            imageView.setImageResource(R.drawable.logo_wake_72);
            textView.setText("Hibernate: " + "OFF");
        }
    }

    public void updateBatteryLevel(int value){
        TextView textView = (TextView) mActivity.findViewById(R.id.percentage_bat);
        ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.progressbar_bat);
        textView.setText("" + value + "%");
        progressBar.setProgress(value);
    }

    public void updateIPAddress(final String ip){

        final TextView textView = mActivity.findViewById(R.id.text_ip);

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                textView.setText("IP: " + ip);
            }
        });

    }

    public void updateGpsObject(){
        ImageView   imageView   = mActivity.findViewById(R.id.imview_loc);
        Button      button      = mActivity.findViewById(R.id.button_gps);

        if(!mIsGPS) {
            button.setText("Stop GPS");
            imageView.setVisibility(View.VISIBLE);
            button.setText("GPS: " + "ON");
            mIsGPS = true;
        }else{
            button.setText("Start GPS");
            imageView.setVisibility(View.INVISIBLE);
            button.setText("GPS: " + "OFF");
            mIsGPS = false;
        }

    }

    public void refreshChart(){

        long endTime = System.currentTimeMillis();

        long delta = endTime - mStartTime;

        if(delta >= 10000){
            mGraphView.removeAllSeries();
            mGraphView.addSeries(mSeries1);
            mGraphView.addSeries(mSeries2);
            mGraphView.addSeries(mSeries3);
            mStartTime = endTime;
        }


    }

    public void updateClipping(){

        TextView textView = mActivity.findViewById(R.id.text_clipping);

        textView.setText("Clipping : " + (++mClippingCount));
    }

    public void updateOverFlow(){

        TextView textView = mActivity.findViewById(R.id.text_overflow);

        textView.setText("Overflow : " + (++mOverflowCount));
    }

    public void displayToast(){

        Toast.makeText(mActivity, "Note received", Toast.LENGTH_LONG).show();
    }

    public boolean ismIsRecord() {
        return mIsRecord;
    }

    public void setmIsRecord(boolean mIsRecord) {
        this.mIsRecord = mIsRecord;
    }

    public boolean ismIsHibernate() {
        return mIsHibernate;
    }

    public void setmIsHibernate(boolean mIsHibernate) {
        this.mIsHibernate = mIsHibernate;
    }

    public boolean ismIsGPS() {
        return mIsGPS;
    }

    public void setmIsGPS(boolean mIsGPS) {
        this.mIsGPS = mIsGPS;
    }

    public PlotDatas getmPlotDatas() {
        return mPlotDatas;
    }

    public int getmSelectedGraphPosition() {
        return mSelectedGraphPosition;
    }

    public HashMap<String, TypesDatas> getmMapGraphSelector() {
        return mMapGraphSelector;
    }
}
