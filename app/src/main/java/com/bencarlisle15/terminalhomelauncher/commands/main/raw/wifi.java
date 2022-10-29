package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;

public class wifi implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        if (info.wifi == null)
            info.wifi = (WifiManager) info.context.getSystemService(Context.WIFI_SERVICE);
        boolean active = !info.wifi.isWifiEnabled();
        info.wifi.setWifiEnabled(active);
        return info.res.getString(R.string.output_wifi) + " " + active;
    }

    @Override
    public int helpRes() {
        return R.string.help_wifi;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

}
