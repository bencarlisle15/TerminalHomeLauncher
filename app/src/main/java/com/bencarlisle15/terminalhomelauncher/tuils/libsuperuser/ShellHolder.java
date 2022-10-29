package com.bencarlisle15.terminalhomelauncher.tuils.libsuperuser;

import android.content.Context;

import java.io.File;
import java.util.regex.Pattern;

import com.bencarlisle15.terminalhomelauncher.managers.TerminalManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.XMLPrefsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Behavior;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

/**
 * Created by francescoandreuzzi on 18/08/2017.
 */

public class ShellHolder {

    private final Context context;

    public ShellHolder(Context context) {
        this.context = context;
    }

    final Pattern p = Pattern.compile("^\\n");

    public Shell.Interactive build() {
        Shell.Interactive interactive = new Shell.Builder()
                .setOnSTDOUTLineListener(line -> {
                    line = p.matcher(line).replaceAll(Tuils.EMPTYSTRING);
                    Tuils.sendOutput(context, line, TerminalManager.CATEGORY_OUTPUT);
                })
                .setOnSTDERRLineListener(line -> {
                    line = p.matcher(line).replaceAll(Tuils.EMPTYSTRING);
                    Tuils.sendOutput(context, line, TerminalManager.CATEGORY_OUTPUT);
                })
                .open();
        interactive.addCommand("cd " + XMLPrefsManager.get(File.class, Behavior.home_path));
        return interactive;
    }
}
