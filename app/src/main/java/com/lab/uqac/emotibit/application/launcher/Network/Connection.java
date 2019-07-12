package com.lab.uqac.emotibit.application.launcher.Network;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.lab.uqac.emotibit.application.launcher.Datas.MessageGenerator;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import GUI.EmotiBitButton;

public class Connection {

    private int mPort;
    private InetAddress mAddress;
    private DatagramSocket mSocket = null;
    private int mMaxConnectedDevice;


    public Connection(int port, int maxConnectedDevice) {
        mPort = port;
        mMaxConnectedDevice = maxConnectedDevice;
    }

    public Connection(int port, DatagramSocket socket, InetAddress address, int maxConnectedDevice) {
        mPort = port;
        mSocket = socket;
        mAddress = address;
        mMaxConnectedDevice = maxConnectedDevice;
    }

    public Connection(int port, InetAddress address, int maxConnectedDevice) {
        mPort = port;
        mAddress = address;
        mMaxConnectedDevice = maxConnectedDevice;
    }


    public Connection(DatagramSocket socket, InetAddress address, int port) {
        mSocket = socket;
        mPort = port;
        mAddress = address;
    }


    public int getmMaxConnectedDevice() {
        return mMaxConnectedDevice;
    }

    public void setmMaxConnectedDevice(int mMaxConnectedDevice) {
        this.mMaxConnectedDevice = mMaxConnectedDevice;
    }

    public int getmPort() {
        return mPort;
    }

    public void setmPort(int mPort) {
        this.mPort = mPort;
    }

    public InetAddress getmAddress() {
        return mAddress;
    }

    public void setmAddress(InetAddress mAddress) {
        this.mAddress = mAddress;
    }

    public DatagramSocket getmSocket() {
        return mSocket;
    }

    public void setmSocket(DatagramSocket mSocket) {
        this.mSocket = mSocket;
    }


}
