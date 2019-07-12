package com.lab.uqac.emotibit.application.launcher.Datas;

public class HeaderDatas {

    private int mTimeStamp;
    private int mPacketNumber;
    private int mDataLength;
    private TypesDatas mTypesDatas;
    private int mProtocolVersion;
    private int mDataReliability;

    public static final int HEADER_SIZE = 6;

    public HeaderDatas(){

    }

    public HeaderDatas(int timeStamp, int packeNumber, int dataLength,
                       TypesDatas typesDatas, int protocolVersion, int dataReliability){

         mTimeStamp = timeStamp;
         mPacketNumber = packeNumber;
         mDataLength = dataLength;
         mTypesDatas = typesDatas;
         mProtocolVersion = protocolVersion;
         mDataReliability = dataReliability;
    }

    public int getmTimeStamp() {
        return mTimeStamp;
    }

    public int getmPacketNumber() {
        return mPacketNumber;
    }

    public int getmDataLength() {
        return mDataLength;
    }

    public TypesDatas getmTypesDatas() {
        return mTypesDatas;
    }

    public int getmProtocolVersion() {
        return mProtocolVersion;
    }

    public int getmDataReliability() {
        return mDataReliability;
    }

    public void setmTag(TypesDatas typesDatas) {
        this.mTypesDatas = typesDatas;
    }

    public void setmPacketNumber(int mPacketNumber) {
        this.mPacketNumber = mPacketNumber;
    }

    public void setmDataLength(int mDataLength) {
        this.mDataLength = mDataLength;
    }

    public void setmProtocolVersion(int mProtocolVersion) {
        this.mProtocolVersion = mProtocolVersion;
    }

    public void setmDataReliability(int mDataReliability) {
        this.mDataReliability = mDataReliability;
    }

    public void setmTimeStamp(int mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}
