package com.lab.uqac.emotibit.application.launcher.Network.Link;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.lab.uqac.emotibit.application.launcher.EmotiBitActivity;
import com.lab.uqac.emotibit.application.launcher.MainActivity;
import com.lab.uqac.emotibit.application.launcher.Network.Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import GUI.EmotiBitButton;

public class LinkEmotiBitReceptor extends AsyncTask<Void, InetAddress, Void> {

    private Connection mConnection;
    private EmotiBitButton mEmotiBitButton;
    private int mIndex = 0;
    private int mMaxConnectedDevice;
    private DatagramSocket mSocket;
    private int mPort;
    private List<InetAddress> mInetAddresses;
    private MainActivity mActivity;
    HandlerThread mHandlerThread;

    Handler mHandler;

    public LinkEmotiBitReceptor(Connection connection, EmotiBitButton emotiBitButton){
        mConnection = connection;
        mPort = mConnection.getmPort();
        mMaxConnectedDevice = mConnection.getmMaxConnectedDevice();
        mHandlerThread = new HandlerThread("threadname");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mInetAddresses = new ArrayList<InetAddress>();
        mEmotiBitButton = emotiBitButton;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            mSocket = new DatagramSocket(mPort);
            mSocket.setBroadcast(true);

            mConnection.setmSocket(mSocket);

            LinkEmotibitEmettor emotibitEmettor = new LinkEmotibitEmettor(mConnection);

            mHandler.postDelayed(emotibitEmettor, 5000);

            while (mIndex < mMaxConnectedDevice) {


                byte[] bufferDatas = new byte[10000];
                DatagramPacket datagramPacketReceive = new DatagramPacket(bufferDatas, bufferDatas.length);

                mSocket.receive(datagramPacketReceive);

                String datas = new String(bufferDatas, 0, datagramPacketReceive.getLength());

                InetAddress inetAddress = datagramPacketReceive.getAddress();

                String helloMessage = emotibitEmettor.getmHelloMessage();

                if (helloMessage != null &&!helloMessage.equals(datas) &&
                        !mInetAddresses.contains(inetAddress)) {
                    mInetAddresses.add(inetAddress);
                    publishProgress(inetAddress);
                    mIndex++;
                }

                if (isCancelled()) {
                    mHandler.removeCallbacks(emotibitEmettor);
                    stopConnection();
                    break;
                }

                mHandler.postDelayed(emotibitEmettor, 10000);

            }

            mHandler.removeCallbacks(emotibitEmettor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(InetAddress... inetAddresses) {
        super.onProgressUpdate(inetAddresses[0]);

        mEmotiBitButton.add(inetAddresses[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        stopConnection();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void stopConnection() {
        if(mSocket != null) {
            mSocket.close();
            mSocket.disconnect();
        }
    }

    public void start(){

        if (this != null && this.getStatus() == AsyncTask.Status.FINISHED ||
                this.getStatus() == AsyncTask.Status.PENDING)
            this.execute();

    }

    public void stop(){

        if (this != null && this.getStatus() != AsyncTask.Status.FINISHED )
            this.cancel(true);

        //stopConnection();
    }
}
