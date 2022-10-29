package com.bencarlisle15.terminalhomelauncher.managers.xml.classes;

import androidx.annotation.NonNull;

/**
 * Created by francescoandreuzzi on 06/03/2018.
 */

public class XMLPrefsEntry {

    public final String key;
    public final String value;

    public XMLPrefsEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof XMLPrefsEntry) return this == obj;
        else if(obj instanceof XMLPrefsSave) return this.key.equals(((XMLPrefsSave) obj).label());
        return obj.equals(key);
    }

    @NonNull
    @Override
    public String toString() {
        return key + " --> " + value;
    }
}