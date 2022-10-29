package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

/**
 * Created by francescoandreuzzi on 21/05/2017.
 */

public class Exit implements CommandAbstraction {
    @Override
    public String exec(ExecutePack pack) throws Exception {
        Tuils.resetPreferredLauncherAndOpenChooser(pack.context);
        return null;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 4;
    }

    @Override
    public int helpRes() {
        return R.string.help_exit;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int indexNotFound) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        return null;
    }
}
