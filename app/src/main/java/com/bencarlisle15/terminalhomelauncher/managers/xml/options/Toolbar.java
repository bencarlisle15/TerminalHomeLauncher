package com.bencarlisle15.terminalhomelauncher.managers.xml.options;

import com.bencarlisle15.terminalhomelauncher.managers.xml.XMLPrefsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.classes.XMLPrefsElement;
import com.bencarlisle15.terminalhomelauncher.managers.xml.classes.XMLPrefsSave;

/**
 * Created by francescoandreuzzi on 24/09/2017.
 */

public enum Toolbar implements XMLPrefsSave {

    show_toolbar {
        @Override
        public String defaultValue() {
            return "true";
        }

        @Override
        public String info() {
            return "If false, the toolbar is hidden";
        }

        @Override
        public String type() {
            return XMLPrefsSave.BOOLEAN;
        }
    },
    hide_toolbar_no_input {
        @Override
        public String defaultValue() {
            return "false";
        }

        @Override
        public String info() {
            return "If true, the toolbar will be hidden when the input field is empty";
        }

        @Override
        public String type() {
            return XMLPrefsSave.BOOLEAN;
        }
    };

    @Override
    public XMLPrefsElement parent() {
        return XMLPrefsManager.XMLPrefsRoot.TOOLBAR;
    }

    @Override
    public String label() {
        return name();
    }

    @Override
    public String[] invalidValues() {
        return null;
    }

    @Override
    public String getLowercaseString() {
        return label();
    }

    @Override
    public String getString() {
        return label();
    }
}