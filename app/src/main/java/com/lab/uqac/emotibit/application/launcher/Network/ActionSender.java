package com.lab.uqac.emotibit.application.launcher.Network;

import android.os.AsyncTask;

import com.lab.uqac.emotibit.application.launcher.Datas.MessageGenerator;
import com.lab.uqac.emotibit.application.launcher.Datas.TypesDatas;

import java.io.IOException;
import java.net.DatagramPacket;

import GUI.UpdateUI;

public class ActionSender extends AsyncTask<Void, Void, Void> {

    private MessageGenerator mMessageGenerator;
    private Connection mConnection;
    private UpdateUI mUpdateUI;

    public ActionSender(MessageGenerator messageGenerator, Connection connection){
        mMessageGenerator = messageGenerator;
        mConnection = connection;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            String action = mMessageGenerator.generateMessage();

            byte[] buffer = action.getBytes();

            DatagramPacket datagramPacketSend = new DatagramPacket(buffer, buffer.length,
                    mConnection.getmAddress(), mConnection.getmPort());

            mConnection.getmSocket().send(datagramPacketSend);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
