package com.bencarlisle15.terminalhomelauncher.commands;

import android.content.Context;
import android.os.Build;

import com.bencarlisle15.terminalhomelauncher.commands.main.specific.APICommand;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommandGroup {

    private CommandAbstraction[] commands;
    private String[] commandNames;

    public CommandGroup(Context c, String packageName) {

        List<String> commands;
        try {
            commands = Tuils.getClassesInPackage(packageName, c);
        } catch (IOException e) {
            return;
        }

        List<CommandAbstraction> cmdAbs = new ArrayList<>();

        Collections.sort(commands);

        Iterator<String> iterator = commands.iterator();

        List<String> commandNamesList = new ArrayList<>();

        while (iterator.hasNext()) {
            String s = iterator.next();
            CommandAbstraction ca = buildCommand(s);

            if (ca != null && (!(ca instanceof APICommand) || ((APICommand) ca).willWorkOn(Build.VERSION.SDK_INT))) {
                cmdAbs.add(ca);
                commandNamesList.add(ca.getCommandName());
            }
        }

        commandNames = commandNamesList.toArray(new String[0]);

        cmdAbs.sort((o1, o2) -> o2.priority() - o1.priority());
        this.commands = new CommandAbstraction[cmdAbs.size()];
        cmdAbs.toArray(this.commands);
    }

    public CommandAbstraction getCommandByName(String name) {
        for (CommandAbstraction c : commands) {
            if (c.getCommandName().equals(name)) {
                return c;
            }
        }

        return null;
    }

    private CommandAbstraction buildCommand(String fullCmdName) {
        try {
            Class<?> clazz = Class.forName(fullCmdName);
            if (CommandAbstraction.class.isAssignableFrom(clazz)) {
                Class<? extends CommandAbstraction> clazz2 = clazz.asSubclass(CommandAbstraction.class);
                Constructor<? extends CommandAbstraction> constructor = clazz2.getConstructor();
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
