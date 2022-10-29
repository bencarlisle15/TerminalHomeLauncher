package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.content.Intent;
import android.net.Uri;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;

public class Rate implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        final MainPack info = (MainPack) pack;
        try {
            info.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                    info.context.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            info.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +
                    info.context.getPackageName())));
        }

        return info.res.getString(R.string.output_rate);
    }

    @Override
    public int helpRes() {
        return R.string.help_rate;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 3;
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
