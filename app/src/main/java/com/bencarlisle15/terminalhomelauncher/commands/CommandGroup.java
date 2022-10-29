package com.bencarlisle15.terminalhomelauncher.commands;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bencarlisle15.terminalhomelauncher.commands.main.specific.APICommand;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

public class CommandGroup {

    private final String packageName;
    private CommandAbstraction[] commands;
    private String[] commandNames;

    public CommandGroup(Context c, String packageName) {
        this.packageName = packageName;

        List<String> cmds;
        try {
            cmds = Tuils.getClassesInPackage(packageName, c);
        } catch (IOException e) {
            return;
        }

        List<CommandAbstraction> cmdAbs = new ArrayList<>();
        Iterator<String> iterator = cmds.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            CommandAbstraction ca = buildCommand(s);
            if(ca != null && ( !(ca instanceof APICommand) || ((APICommand) ca).willWorkOn(Build.VERSION.SDK_INT))) {
                cmdAbs.add(ca);
            } else {
                iterator.remove();
            }
        }

        Collections.sort(cmds);
        commandNames = new String[cmds.size()];
        for (int i = 0; i < commandNames.length; i++) {
            commandNames[i] = cmds.get(i).toLowerCase();
        }

        cmdAbs.sort((o1, o2) -> o2.priority() - o1.priority());
        commands = new CommandAbstraction[cmdAbs.size()];
        cmdAbs.toArray(commands);
    }

    public CommandAbstraction getCommandByName(String name) {
        for(CommandAbstraction c : commands) {
            if(c.getClass().getSimpleName().equals(name)) {
                return c;
            }
        }

        return null;
    }

    private CommandAbstraction buildCommand(String name) {
        String fullCmdName = packageName + Tuils.DOT + name;
        try {
            Class<CommandAbstraction> clazz = (Class<CommandAbstraction>) Class.forName(fullCmdName);
            if(CommandAbstraction.class.isAssignableFrom(clazz)) {
                Constructor<CommandAbstraction> constructor = clazz.getConstructor();
                return constructor.newInstance();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public CommandAbstraction[] getCommands() {
        return commands;
    }

    public String[] getCommandNames() {
        return commandNames;
    }

}
