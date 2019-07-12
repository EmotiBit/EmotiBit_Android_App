package com.lab.uqac.emotibit.application.launcher.Drawing;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;

public class PlotDatas {


    private  LineGraphSeries<DataPoint> mSeries1;
    private  LineGraphSeries<DataPoint> mSeries2;
    private  LineGraphSeries<DataPoint> mSeries3;
    private  GraphView mGraphView;
    private  double x = 0;

    public PlotDatas(GraphView graphView, LineGraphSeries<DataPoint> series1,
                      LineGraphSeries<DataPoint> series2,
                      LineGraphSeries<DataPoint> series3){

        mSeries1 = series1;
        mSeries2 = series2;
        mSeries3 = series3;
        mGraphView = graphView;
    }


    private static Double calculateYmin(Object[] values) {

        double min = Double.valueOf((String) values[0]);
        for (int k = 0; k < values.length; k++) {
            double value = Double.valueOf((String)values[k]);
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private static Double calculateYmax(Object[] values) {

        double max = Double.valueOf((String) values[0]);
        for (int k = 0; k < values.length; k++) {
            double value = Double.valueOf((String)values[k]);
            if ( value > max) {
                max = value;
            }
        }
        return max;
    }


    public void plot(TypesDatas typesDatas, final Object[] values){

        switch (typesDatas){

            case ACCX :
                for (int i = 0; i < values.length; i++){
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-4);
                    mGraphView.getViewport().setMaxY(4);
                    mSeries1.setTitle("Ax");
                    mGraphView.getLegendRenderer().setVisible(true);

                    x = x + 0.1;
                }
                break;

            case ACCY :

                for (int i = 0; i < values.length; i++){
                    Double ayDataValue = Double.valueOf((String)values[i]);
                    mSeries2.appendData(new DataPoint(x, ayDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-4);
                    mGraphView.getViewport().setMaxY(4);
                    mSeries2.setTitle("Ay");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }

                break;

            case ACCZ :

                for (int i = 0; i < values.length; i++){
                    Double azDataValue = Double.valueOf((String)values[i]);
                    mSeries3.appendData(new DataPoint(x, azDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-4);
                    mGraphView.getViewport().setMaxY(4);
                    mSeries3.setTitle("Az");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }

                break;

            case GYROX :
                for (int i = 0; i < values.length; i++){
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-500);
                    mGraphView.getViewport().setMaxY(500);
                    mSeries1.setTitle("Gx");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }
                break;


            case GYROY :
                for (int i = 0; i < values.length; i++){
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries2.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-500);
                    mGraphView.getViewport().setMaxY(500);
                    mSeries2.setTitle("Gy");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }
                break;
            case GYROZ :
                for (int i = 0; i < values.length; i++){
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries3.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-500);
                    mGraphView.getViewport().setMaxY(500);
                    mSeries3.setTitle("Gz");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }
                break;

            case MAGNX :
                for (int i = 0; i < values.length; i++){
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-4800);
                    mGraphView.getViewport().setMaxY(4800);
                    mSeries1.setTitle("Mx");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }
                break;

            case MAGNY :
                for (int i = 0; i < values.length; i++){
                    Double ayDataValue = Double.valueOf((String)values[i]);
                    mSeries2.appendData(new DataPoint(x, ayDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-4800);
                    mGraphView.getViewport().setMaxY(4800);
                    mSeries2.setTitle("My");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }
                break;

            case MAGNZ :
                for (int i = 0; i < values.length; i++){
                    Double azDataValue = Double.valueOf((String)values[i]);
                    mSeries3.appendData(new DataPoint(x, azDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getViewport().setMinY(-4800);
                    mGraphView.getViewport().setMaxY(+4800);
                    mSeries3.setTitle("Mz");
                    mGraphView.getLegendRenderer().setVisible(true);
                    x = x + 0.1;
                }
                break;

            case HUM :

                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;

            case EDA :

                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.parseDouble((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;

            case PPGGRN :
                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;

            case PPGIR :

                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;

            case PPGR :

                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;

            case TEMP :

                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.parseDouble((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;

            case THERM :

                for (int i = 0; i < values.length; i++) {
                    Double axDataValue = Double.valueOf((String) values[i]);
                    mSeries1.appendData(new DataPoint(x, axDataValue), true, 100);
                    mGraphView.getViewport().setXAxisBoundsManual(true);
                    mGraphView.getViewport().setMinX(x <= 10 ? 0 : x - 10);
                    mGraphView.getViewport().setMaxX(x);
                    mGraphView.getLegendRenderer().setVisible(false);
                    x = x + 0.1;
                }

                break;
        }

    }

}
