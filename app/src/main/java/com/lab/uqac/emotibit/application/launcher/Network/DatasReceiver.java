package com.lab.uqac.emotibit.application.launcher.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.lab.uqac.emotibit.application.launcher.Datas.ExtractedDatas;
import com.lab.uqac.emotibit.application.launcher.Datas.ExtractionDatas;
import com.lab.uqac.emotibit.application.launcher.Datas.MessageGenerator;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import GUI.UpdateUI;

public class DatasReceiver extends AsyncTask<Void, String, Void>{

    private DatagramSocket mSocket;
    private int mPort;
    private InetAddress mInetAdress;
    private UpdateUI mUpdateUI;
    private ExtractionDatas mExtraction;
    private HashMap<String, TypesDatas> mMapGraphSelector;
    private Connection mConnection;

    public DatasReceiver(InetAddress inetAddress, int port)
    {
        mInetAdress = inetAddress;
        mPort = port;
    }

    public DatasReceiver(Connection connection, UpdateUI updateUI)
    {
        mConnection = connection;
        mInetAdress = mConnection.getmAddress();
        mPort = mConnection.getmPort();
        mUpdateUI = updateUI;
        mMapGraphSelector = mUpdateUI.getmMapGraphSelector();
        mConnection = connection;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        try {

            Thread.sleep(1000);

            if(mSocket == null) {
                mSocket = new DatagramSocket(mPort);
                mSocket.connect(mInetAdress, mPort);
                mConnection.setmSocket(mSocket);
                mUpdateUI.updateIPAddress(mInetAdress.getHostName());
            }

            while (mSocket != null && mSocket.isConnected() && !mUpdateUI.ismIsHibernate()) {

                byte[] buf = new byte[10000];
                DatagramPacket datagramPacketReceive = new DatagramPacket(buf, buf.length);
                mSocket.receive(datagramPacketReceive);

                String datas = new String(buf, 0, datagramPacketReceive.getLength());

                publishProgress(datas);

                if(isCancelled())
                {
                    mSocket.close();
                    break;
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... datas) {
        super.onProgressUpdate(datas[0]);

        mExtraction = new ExtractionDatas(datas[0]);

        ArrayList<ExtractedDatas> extractedDatas = mExtraction.extractDatas();

        for (ExtractedDatas extrDatas : extractedDatas) {

            String dataType = extrDatas.getmDataType();

            TypesDatas typesDatas = TypesDatas.getValueMap(mMapGraphSelector, dataType);

            Object[] values = extrDatas.getmValues();

            int selected = typesDatas.getmSelectedGraph();

            if(selected == mUpdateUI.getmSelectedGraphPosition())
                mUpdateUI.getmPlotDatas().plot(typesDatas, values);

            if(dataType.equals("AK")){

                Log.d("RECEPTION_ACTIVITY", " - -----  - ----- --- -- datas = " + datas[0]);

                if(values[1].equals("RB")) {
                    mUpdateUI.updateButtonRecord();
                }
                else if(values[1].equals("RE")) {
                    mUpdateUI.updateButtonRecord();
                }
                else if(values[1].equals("MH")) {
                    mUpdateUI.updateHibernateState();

                }else if(values[1].equals("UN")) {
                    mUpdateUI.displayToast();
                }

            }else if(dataType.equals("DC")){

                mUpdateUI.updateClipping();

            }else if(dataType.equals("DO")){
                mUpdateUI.updateOverFlow();

            }else if(dataType.equals("RD")){

                MessageGenerator messageGenerator = new MessageGenerator(TypesDatas.TL, 1,
                        1, 100, "", false);
                ActionSender actionSender = new ActionSender(messageGenerator, mConnection);
                actionSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                messageGenerator = new MessageGenerator(TypesDatas.TU, 1,
                        1, 100, "", true);

                actionSender = new ActionSender(messageGenerator, mConnection);
                actionSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }else if(dataType.equals("B%")){
                String value = (String)values[0];
                mUpdateUI.updateBatteryLevel(Integer.valueOf(value.trim()));
            }

            mUpdateUI.refreshChart();
        }
    }

    public void start(){

        if (this != null && this.getStatus() == Status.FINISHED ||
                this.getStatus() == Status.PENDING)
            this.execute();

    }

    public void stop(){

        if (this != null && this.getStatus() != Status.FINISHED )
            this.cancel(true);
    }

    public DatagramSocket getmSocket() {
        return mSocket;
    }

    public int getmPort() {
        return mPort;
    }

    public InetAddress getmInetAdress() {
        return mInetAdress;
    }
}
