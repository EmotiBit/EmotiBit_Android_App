package com.lab.uqac.emotibit.application.launcher.Datas;

import java.util.ArrayList;

public class ExtractionDatas  {

    private String mDatas;
    private String[] mDatasLines;
    private int mLinesNumber;

    public static final int HEADER_SIZE = 6;

    public ExtractionDatas(String datas){

        mDatas = datas;
        mDatasLines = mDatas.split("\n");
        mLinesNumber = mDatasLines.length;
    }

    public ArrayList<ExtractedDatas> extractDatas() {


            String[] dataLine = null;
            Object[] values = null;
            ArrayList<ExtractedDatas> extractedDatas = new ArrayList<ExtractedDatas>();
            String temp = new String(mDatas);

            while (!temp.equals("") && temp != null && !temp.equals("\0")) {

                int endLineIndex = temp.indexOf("\n");

                if(endLineIndex == 0){ // modified by nitin
                    temp = temp.substring(endLineIndex + 1);
                }

                else if (endLineIndex != -1 ) {
                    String line = temp.substring(0, endLineIndex);

                    temp = temp.substring(endLineIndex + 1);

                    dataLine = line.split(",");

                    String dataType = dataLine[3];

                    values = new Object[dataLine.length - HEADER_SIZE];

                    for (int j = HEADER_SIZE; j < dataLine.length; j++) {

                        values[j - HEADER_SIZE] = dataLine[j];
                    }

                    extractedDatas.add(new ExtractedDatas(dataType, values));

                }else
                    temp = "";

            }

            return extractedDatas;
    }



    public String getmDatas() {
        return mDatas;
    }

    public String[] getmDatasLines() {
        return mDatasLines;
    }

    public int getmLinesNumber() {
        return mLinesNumber;
    }

    public void setmDatas(String mDatas) {
        this.mDatas = mDatas;
    }

    public void setmDatasLines(String[] mDatasLines) {
        this.mDatasLines = mDatasLines;
    }

    public void setmLinesNumber(int mLinesNumber) {
        this.mLinesNumber = mLinesNumber;
    }

}
