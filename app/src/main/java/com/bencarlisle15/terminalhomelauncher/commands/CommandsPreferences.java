package com.bencarlisle15.terminalhomelauncher.commands;

import com.bencarlisle15.terminalhomelauncher.managers.xml.XMLPrefsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.classes.XMLPrefsSave;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Cmd;

import java.util.HashMap;

/**
 * Created by francescoandreuzzi on 06/01/2017.
 */

public class CommandsPreferences {

    public static final String PRIORITY_SUFFIX = "_priority";

    private final HashMap<String, String> preferenceHashMap;

    public CommandsPreferences() {
        preferenceHashMap = new HashMap<>();

        for (XMLPrefsSave save : Cmd.values()) {
            preferenceHashMap.put(save.label(), XMLPrefsManager.get(save));
        }
    }

    public String get(String s) {
        String v = preferenceHashMap.get(s);
        if (v == null) return XMLPrefsManager.get(XMLPrefsManager.XMLPrefsRoot.CMD, s);
        return v;
    }

    public String get(XMLPrefsSave save) {
        String v = get(save.label());
        if (v == null || v.length() == 0) v = save.defaultValue();
        return v;
    }

    public int userSetPriority(CommandAbstraction c) {
        try {
            String p = get(c.getClass().getSimpleName() + PRIORITY_SUFFIX);
            return Integer.parseInt(p);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public int getPriority(CommandAbstraction c) {
        int priority = userSetPriority(c);
        if (priority == Integer.MAX_VALUE) return c.priority();
        return priority;
    }
}
