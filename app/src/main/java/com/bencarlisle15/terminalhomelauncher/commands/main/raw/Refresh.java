package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;

public class Refresh implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        info.appsManager.fill();
        info.aliasManager.reload();
        if (info.player != null) info.player.refresh();
        info.contacts.refreshContacts(info.context);
        info.rssManager.refresh();

        return info.res.getString(R.string.output_refresh);
    }

    @Override
    public int helpRes() {
        return R.string.help_refresh;
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
