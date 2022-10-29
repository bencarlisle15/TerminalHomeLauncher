package com.bencarlisle15.terminalhomelauncher.commands.tuixt;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import java.io.File;

import com.bencarlisle15.terminalhomelauncher.commands.CommandGroup;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;

/**
 * Created by francescoandreuzzi on 25/01/2017.
 */

public class TuixtPack extends ExecutePack {

    public final File editFile;
    public final EditText editText;

    public final Resources resources;

    public TuixtPack(CommandGroup group, File file, Context context, EditText editText) {
        super(group);

        this.editText = editText;
        editFile = file;
        this.context = context;
        this.resources = context.getResources();
    }
}
