package GUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lab.uqac.emotibit.application.launcher.EmotiBitActivity;
import com.lab.uqac.emotibit.application.launcher.MainActivity;
import com.lab.uqac.emotibit.application.launcher.R;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EmotiBitButton {

    private int mIndex = 0;
    private HashMap<InetAddress, View> mMapButton;
    private Activity mActivity;
    private LinearLayout mVerticalLayout;
    private LinearLayout.LayoutParams mParams;
    private String mTextButton;
    private List<InetAddress> mSavedList = new ArrayList<InetAddress>();
    private int mMaxDevice;

    public EmotiBitButton(Activity activity, int maxDevice){
        mActivity = activity;
        mVerticalLayout = mActivity.findViewById(R.id.row_main);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        mMaxDevice = maxDevice;
        mMapButton = new HashMap<InetAddress, View>();
    }

    public void add(final InetAddress inetAddress) {

        if(!mMapButton.containsKey(inetAddress)) {

            Button button = new Button(mActivity);
            // **1 was defined here -nitin

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextButton = ((Button)v).getText().toString();
                    Intent intent = new Intent(mActivity, EmotiBitActivity.class);

                    intent.putExtra("selected", mTextButton );

                    intent.putExtra("address", inetAddress);

                    mActivity.startActivity(intent);
                }
            });
//            button.setText("EmotiBit " + (mIndex + 1));//**1
            button.setText("EmotiBit " + (inetAddress));

            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.logo_wake_72, 0);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);

            layoutParams.height = 50;

            layoutParams.width = 50;

            button.setLayoutParams(layoutParams);

            mVerticalLayout.addView(button, mParams);

            mParams.setMargins(0, 50, 0, 0);

            mMapButton.put(inetAddress, button);
            mSavedList.add(inetAddress);
            mIndex++;
        }
    }


   public void redrawButtons(){

        for (final InetAddress inetAddress : mSavedList) {

            Button button = new Button(mActivity);
            button.setText("EmotiBit " + (mIndex + 1));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextButton = ((Button)v).getText().toString();
                    Intent intent = new Intent(mActivity, EmotiBitActivity.class);

                    intent.putExtra("selected", mTextButton );

                    intent.putExtra("address", inetAddress);

                    mActivity.startActivity(intent);
                }
            });

            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.logo_wake_72, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);

            layoutParams.height = 50;

            layoutParams.width = 50;

            button.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout verticalLayout = mActivity.findViewById(R.id.row_main);
            verticalLayout.addView(button, params);

            params.setMargins(0, 50, 0, 0);
            mIndex++;
        }
    }

    public HashMap<InetAddress, View> getmMapButton() {
        return mMapButton;
    }

}
