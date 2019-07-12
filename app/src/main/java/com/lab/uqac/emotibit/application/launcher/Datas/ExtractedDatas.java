package com.lab.uqac.emotibit.application.launcher.Datas;

public class ExtractedDatas {

    private String mDataType;
    private Object[] mValues;

    public ExtractedDatas(String dataType, Object[] values){

            mDataType     = dataType;
            mValues         = values;
    }

    public String getmDataType() {
        return mDataType;
    }

    public void setmDataType(String mDataType) {
        this.mDataType = mDataType;
    }

    public Object[] getmValues() {
        return mValues;
    }

    public void setmValues(Object[] mValues) {
        this.mValues = mValues;
    }

}
