package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.commands.main.specific.APICommand;

/**
 * Created by bencarlisle15 on 03/12/15.
 */
public class airplane implements APICommand, CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        return null;
    }

    private boolean isEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int helpRes() {
        return R.string.help_airplane;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public boolean willWorkOn(int api) {
        return api < Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
