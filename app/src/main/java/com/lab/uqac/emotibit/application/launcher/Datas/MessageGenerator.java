package com.lab.uqac.emotibit.application.launcher.Datas;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MessageGenerator {


    public static String DATE_FORMAT = "y-MM-dd_HH-mm-ss-SSS";

    static int mPacketNumber = 0;
    private TypesDatas      mTypeDatas;
    private int             mDataLength;
    private int             mProtocolVersion;
    private int             mReliability;
    private String          mDatas;
    private boolean         mUTC;

    public MessageGenerator(TypesDatas typesDatas, int dataLength,
                            int protocolVersion, int reliability,
                            String datas, boolean utc){

        mTypeDatas = typesDatas;
        mDataLength = dataLength;
        mProtocolVersion = protocolVersion;
        mReliability = reliability;
        mDatas = datas;
        mUTC   = utc;
    }


    public String generateMessage() {

        String message = "";

        message = "" + System.currentTimeMillis();
        message += "," + (++mPacketNumber);
        message += "," + mDataLength;
        message += "," + mTypeDatas.getmTag();
        message += "," + mProtocolVersion;
        message += "," + mReliability;

        message += "," + generateTime();

        if(mTypeDatas == TypesDatas.UN)
            message += "," + mDatas;

        return message;
    }


    public String generateTime(){

        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        if(mUTC)
            dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));

        return dateFormat.format(date);
    }

}
