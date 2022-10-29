package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;

/**
 * Created by francescoandreuzzi on 29/04/2017.
 */

public class Beep implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) throws Exception {
        try {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);
        } catch (Exception e) {
            return e.toString();
        }

        return null;
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
    public int helpRes() {
        return R.string.help_beep;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int index) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        return pack.context.getString(helpRes());
    }
}
