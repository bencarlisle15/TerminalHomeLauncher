package ohi.andre.consolelauncher.commands.main.raw;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.main.specific.APICommand;

/**
 * Created by andre on 03/12/15.
 */
public class airplane implements APICommand, CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        return null;
    }

    private boolean isEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
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
