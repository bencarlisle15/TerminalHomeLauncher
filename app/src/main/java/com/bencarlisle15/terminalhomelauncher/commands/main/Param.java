package com.bencarlisle15.terminalhomelauncher.commands.main;

import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;

/**
 * Created by francescoandreuzzi on 10/06/2017.
 */

public interface Param {

    int[] args();
    String exec(ExecutePack pack);
    String label();

    String onNotArgEnough(ExecutePack pack, int n);
    String onArgNotFound(ExecutePack pack, int index);
}
