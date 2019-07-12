package com.lab.uqac.emotibit.application.launcher.Network.Link;

import android.util.Log;

import com.lab.uqac.emotibit.application.launcher.Datas.MessageGenerator;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;
import com.lab.uqac.emotibit.application.launcher.Network.Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import GUI.EmotiBitButton;

public class LinkEmotibitEmettor implements Runnable{


    private DatagramSocket mSocket;
    private int mPort;
    private InetAddress mAddress;
    private String mHelloMessage;

    public LinkEmotibitEmettor(Connection connection){

        mPort = connection.getmPort();
        mAddress = connection.getmAddress();
        mSocket = connection.getmSocket();
        mHelloMessage = null;
    }

    @Override
    public void run() {
        try {

                if(mHelloMessage == null) {
                    MessageGenerator messageGenerator = new MessageGenerator(TypesDatas.HE,
                            1, 1, 100, "", false);

                    mHelloMessage = messageGenerator.generateMessage();

                }
                byte[] buffer = mHelloMessage.getBytes();
                DatagramPacket datagramPacketSend = new DatagramPacket(buffer, buffer.length,
                            mAddress, mPort);

                mSocket.send(datagramPacketSend);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getmHelloMessage() {
        return mHelloMessage;
    }
}
