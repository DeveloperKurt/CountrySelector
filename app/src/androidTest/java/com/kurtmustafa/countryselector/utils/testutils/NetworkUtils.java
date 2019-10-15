package com.kurtmustafa.countryselector.utils.testutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;


public class NetworkUtils
    {
        /**
         * Checks whether the device has internet connection
         * @param context Application context
         * @return returns true if the device has internet connection and vice versa
         */
        public static boolean haveNetworkConnection(Context context)
            {
                boolean haveConnectedWifi = false;
                boolean haveConnectedMobile = false;

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo)
                    {
                        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                            if (ni.isConnected())
                                haveConnectedWifi = true;
                        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                            if (ni.isConnected())
                                haveConnectedMobile = true;
                    }
                return haveConnectedWifi || haveConnectedMobile;
            }

        /**
         * Disables the wifi and mobile data by using ADB.
         * <p/>
         * NOTE: THIS METHOD REQUIRES A ROOTED DEVICE.
         *
         * @return returns true if the process is sent and terminated, false if device is not rooted
         * @throws IOException
         * @throws InterruptedException
         */
        public static boolean disableInternetViaADBMessages() throws IOException, InterruptedException
            {

                if (isDeviceRooted())
                    {
                        Process wifiProcess = Runtime.getRuntime().exec("adb shell su -c 'svc wifi disable'");
                        wifiProcess.waitFor();
                        Process mobileDataProcess = Runtime.getRuntime().exec("adb shell su -c 'svc data disable'");
                        mobileDataProcess.waitFor();
                        return true;
                    } else
                    {
                        return false;
                    }
            }

        /**
         * @return returns true if the device is rooted.
         */
        private static boolean isDeviceRooted()
            {
                String buildTags = android.os.Build.TAGS;
                return buildTags != null && buildTags.contains("test-keys");
            }
    }
