package com.bencarlisle15.terminalhomelauncher.commands.tuixt.raw;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.tuixt.TuixtPack;
import com.bencarlisle15.terminalhomelauncher.managers.FileManager;

/**
 * Created by francescoandreuzzi on 24/01/2017.
 */

public class save implements CommandAbstraction {

    @Override
    public String exec(ExecutePack info) throws Exception {
        TuixtPack pack = (TuixtPack) info;

        String text = pack.editText.getText().toString();

        String error = FileManager.writeOn(pack.editFile, text);
        if(error == null) {
            return pack.resources.getString(R.string.tuixt_saved);
        } else {
            return error;
        }
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public int helpRes() {
        return R.string.help_tuixt_save;
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
