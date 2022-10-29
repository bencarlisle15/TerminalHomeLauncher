package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

/**
 * Created by francescoandreuzzi on 05/04/16.
 */
public class Status implements CommandAbstraction {

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (Network network: connectivityManager.getAllNetworks()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (Network network: connectivityManager.getAllNetworks()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;

//        wifi
        boolean wifiConnected = isWifiConnected(pack.context);

//        battery
        Intent batteryIntent = info.context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int rawlevel = batteryIntent.getIntExtra("level", -1);
        double scale = batteryIntent.getIntExtra("scale", -1);
        double level = -1;
        if (rawlevel >= 0 && scale > 0) {
            level = rawlevel / scale;
        }
        level *= 100;

//        mobile
        boolean mobileOn = isMobileDataConnected(pack.context);

//        brightness
        ContentResolver cResolver = pack.context.getApplicationContext().getContentResolver();
        int b = 0;
        try {
            b = Settings.System.getInt(cResolver, SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        b = b * 100 / 255;

        int autobrightnessState = Integer.MIN_VALUE;
        try {
            autobrightnessState = Settings.System.getInt(cResolver, SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        location
        LocationManager lm = (LocationManager) pack.context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

//        bluetooth
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        boolean bluetoothOn;

        if(adapter == null) bluetoothOn = false;
        else bluetoothOn = adapter.isEnabled();

        return info.res.getString(R.string.battery_label) + Tuils.SPACE + level + "%" + Tuils.NEWLINE +
                info.res.getString(R.string.wifi_label) + Tuils.SPACE + wifiConnected + Tuils.NEWLINE +
                info.res.getString(R.string.mobile_data_label) + Tuils.SPACE + mobileOn + Tuils.NEWLINE +
                info.res.getString(R.string.bluetooth_label) + Tuils.SPACE + bluetoothOn + Tuils.NEWLINE +
                info.res.getString(R.string.location_label) + Tuils.SPACE + (gps_enabled || network_enabled) + Tuils.NEWLINE +
                info.res.getString(R.string.brightness_label) + Tuils.SPACE + (autobrightnessState == SCREEN_BRIGHTNESS_MODE_AUTOMATIC ? "(auto) " : Tuils.EMPTYSTRING) + b + "%";
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public int helpRes() {
        return R.string.help_status;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }
}
