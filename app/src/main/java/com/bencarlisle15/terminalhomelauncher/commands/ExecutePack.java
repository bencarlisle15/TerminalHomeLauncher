package com.bencarlisle15.terminalhomelauncher.commands;

import android.content.Context;

import com.bencarlisle15.terminalhomelauncher.managers.AppsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.classes.XMLPrefsSave;

import java.util.ArrayList;

public abstract class ExecutePack {

    public Object[] args;
    public Context context;
    public final CommandGroup commandGroup;

    public int currentIndex = 0;

    public ExecutePack(CommandGroup group) {
        this.commandGroup = group;
    }

    @SuppressWarnings("unchecked")
    private <T> T convertToGeneric(Class<T> c, Object result) {
        if (c.isInstance(result)) {
            return (T) result;
        }
        return null;
    }

    public <T> T get(Class<T> c) {
        return convertToGeneric(c, get());
    }

    public <T> T get(Class<T> c, int index) {
        if (index < args.length) {
            return convertToGeneric(c, args[index]);
        }
        return null;
    }

    public Object get() {
        if (currentIndex < args.length) {
            return args[currentIndex++];
        }
        return null;
    }

    public String getString() {
        return (String) get();
    }

    public int getInt() {
        return (int) get();
    }

    public boolean getBoolean() {
        return (boolean) get();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getList() {
        return (ArrayList<String>) get(ArrayList.class);
    }

    public XMLPrefsSave getPrefsSave() {
        return (XMLPrefsSave) get();
    }

    public AppsManager.LaunchInfo getLaunchInfo() {
        return (AppsManager.LaunchInfo) get();
    }

    public void set(Object[] args) {
        this.args = args;
    }

    public void clear() {
        args = null;
        currentIndex = 0;
    }
}
