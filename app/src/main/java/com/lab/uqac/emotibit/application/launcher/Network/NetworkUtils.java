package com.lab.uqac.emotibit.application.launcher.Network;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.lab.uqac.emotibit.application.launcher.EmotiBitActivity;
import com.lab.uqac.emotibit.application.launcher.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

public class NetworkUtils {


    private Context mContext;

    public NetworkUtils(Context context) {
        mContext = context;
    }

    public boolean isPhoneHotspot() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {

        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        boolean isHotspot = false;

        Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
        method.setAccessible(true);
        isHotspot = (boolean) method.invoke(wifiManager);


        return isHotspot;
    }


    public boolean enableHotspot() {

        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);


        try {
            if(isPhoneHotspot()) {
                wifiManager.setWifiEnabled(false);
            }
            Method wifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration",null);
            WifiConfiguration wifiConfiguration = (WifiConfiguration)wifiApConfigurationMethod.invoke(wifiManager, null);
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, wifiConfiguration, !isPhoneHotspot());

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public InetAddress extractBroadcastAddress() throws UnknownHostException, SocketException,
                         NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        InetAddress inetAddress = null;
        InetAddress result = null;
        if(isPhoneHotspot())
        {
            inetAddress = InetAddress.getByName(mContext.getString(R.string.subnet_wifiAp) + 1);
        }else {

            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

            int ipAddress = dhcpInfo.ipAddress;

            if (ipAddress != 0) {
                byte[] ipArray = new byte[4];
                ipArray[0] = (byte) (ipAddress & 0xFF);
                ipArray[1] = (byte) (ipAddress >> 8 & 0xFF);
                ipArray[2] = (byte) (ipAddress >> 16 & 0xFF);
                ipArray[3] = (byte) (ipAddress >> 24 & 0xFF);

                inetAddress = InetAddress.getByAddress(ipArray);
            }else
                return null;

        }


        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);

        List<InterfaceAddress> interfaces = networkInterface.getInterfaceAddresses();

        InterfaceAddress interfaceAddress = interfaces.get(interfaces.size() - 1);

        return interfaceAddress.getBroadcast();

    }



}
