package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.commands.main.specific.ParamCommand;
import com.bencarlisle15.terminalhomelauncher.managers.RegexManager;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

/**
 * Created by francescoandreuzzi on 04/10/2017.
 */

public class Regex extends ParamCommand {

    private enum Param implements com.bencarlisle15.terminalhomelauncher.commands.main.Param {

        add {
            @Override
            public String exec(ExecutePack pack) {
                String output = RegexManager.instance.add(pack.getInt(), pack.getString());
                if(output == null) return null;
                if(output.length() == 0) return pack.context.getString(R.string.id_already);
                else return output;
            }

            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.INT, CommandAbstraction.PLAIN_TEXT};
            }
        },
        rm {
            @Override
            public String exec(ExecutePack pack) {
                String output = RegexManager.instance.rm(pack.getInt());
                if(output == null) return null;
                if(output.length() == 0) return pack.context.getString(R.string.id_notfound);
                return output;
            }

            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.INT};
            }
        },
        get {
            @Override
            public String exec(ExecutePack pack) {
                RegexManager.Regex r = RegexManager.instance.get(pack.getInt());
                if(r == null) return pack.context.getString(R.string.id_notfound);

                return r.regex != null ? r.regex.pattern() : r.literalPattern;
            }

            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.INT};
            }
        },
        test {
            @Override
            public String exec(ExecutePack pack) {
                CharSequence s = RegexManager.instance.test(pack.getInt(), pack.getString());
                if(s.length() == 0) return pack.context.getString(R.string.id_notfound);

                Tuils.sendOutput(pack.context, s);
                return null;
            }

            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.INT, CommandAbstraction.PLAIN_TEXT};
            }
        };

        @Override
        public String onArgNotFound(ExecutePack pack, int index) {
            return pack.context.getString(R.string.invalid_integer);
        }

        @Override
        public String onNotArgEnough(ExecutePack pack, int n) {
            return pack.context.getString(R.string.help_regex);
        }

        @Override
        public String label() {
            return Tuils.MINUS + name();
        }

        static Param get(String p) {
            p = p.toLowerCase();
            Param[] ps = values();
            for (Param p1 : ps)
                if (p.endsWith(p1.label()))
                    return p1;
            return null;
        }

        static String[] labels() {
            Param[] ps = values();
            String[] ss = new String[ps.length];

            for (int count = 0; count < ps.length; count++) {
                ss[count] = ps[count].label();
            }

            return ss;
        }

    }

    @Override
    protected com.bencarlisle15.terminalhomelauncher.commands.main.Param paramForString(MainPack pack, String param) {
        return Param.get(param);
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public int helpRes() {
        return R.string.help_regex;
    }

    @Override
    public String[] params() {
        return Param.labels();
    }
}
