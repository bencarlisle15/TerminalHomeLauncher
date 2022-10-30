package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.commands.main.specific.ParamCommand;
import com.bencarlisle15.terminalhomelauncher.managers.xml.classes.XMLPrefsSave;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Cmd;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

import java.util.List;

public class Search extends ParamCommand {

    private static final String YOUTUBE_PREFIX = "https://www.youtube.com/results?search_query=";
    private static final String YOUTUBE_PACKAGE = "com.google.android.youtube";
    private static final String GOOGLE_PREFIX = "https://www.google.com/search?q=";
    private static final String GOOGLE_PACKAGE = "com.google.android.googlequicksearchbox";
    private static final String GOOGLE_ACTIVITY = ".SearchActivity";
    private static final String PLAYSTORE_PREFIX = "market://search?q=";
    private static final String PLAYSTORE_BROWSER_PREFIX = "https://play.google.com/store/search?q=";
    private static final String DUCKDUCKGO_PREFIX = "https://duckduckgo.com/?q=";
    private static final String DUCKDUCKGO_PACKAGE = "com.duckduckgo.mobile.android";
    private static final String DUCKDUCKGO_ACTIVITY = ".activity.DuckDuckGo";

    private enum Param implements com.bencarlisle15.terminalhomelauncher.commands.main.Param {

        ps {
            @Override
            public String exec(ExecutePack pack) {
                List<String> args = pack.getList();
                return playstore(args, pack.context);
            }
        },
        //        file {
//            @Override
//            public String exec(ExecutePack pack) {
//                List<String> args = pack.get(ArrayList.class, 1);
//                MainPack p = ((MainPack) pack);
//                return file(args, p.currentDirectory, p.res, p.outputable);
//            }
//        },
        gg {
            @Override
            public String exec(ExecutePack pack) {
                List<String> args = pack.getList();
                return google(args, pack.context);
            }
        },
        yt {
            @Override
            public String exec(ExecutePack pack) {
                List<String> args = pack.getList();
                return youTube(args, pack.context);
            }
        },
        u {
            @Override
            public String exec(ExecutePack pack) {
                List<String> args = pack.getList();
                return url(Tuils.toPlanString(args, Tuils.SPACE), pack.context);
            }
        },
        dd {
            @Override
            public String exec(ExecutePack pack) {
                List<String> args = pack.getList();
                return duckDuck(args, pack.context);
            }
        };

        @Override
        public int[] args() {
            return new int[]{CommandAbstraction.TEXTLIST};
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

        @Override
        public String label() {
            return Tuils.MINUS + name();
        }

        @Override
        public String onNotArgEnough(ExecutePack pack, int n) {
            return pack.context.getString(R.string.help_search);
        }

        @Override
        public String onArgNotFound(ExecutePack pack, int index) {
            return null;
        }
    }

    @Override
    protected com.bencarlisle15.terminalhomelauncher.commands.main.Param paramForString(MainPack pack, String param) {
        return Param.get(param);
    }

    @Override
    public XMLPrefsSave defaultParamReference() {
        return Cmd.default_search;
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }

    @Override
    public String[] params() {
        return Param.labels();
    }

    @SuppressWarnings("SameReturnValue")
    private static String google(List<String> args, Context c) {

        try {
            String toSearch = Tuils.toPlanString(args, " ");

            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.setClassName(GOOGLE_PACKAGE, GOOGLE_PACKAGE + GOOGLE_ACTIVITY);
            intent.putExtra(SearchManager.QUERY, toSearch);
            c.startActivity(intent);
        } catch (Exception e) {
            String toSearch = Tuils.toPlanString(args, "+");

            Uri uri = Uri.parse(GOOGLE_PREFIX + toSearch);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            c.startActivity(intent);
        }

        return Tuils.EMPTYSTRING;
    }

    @SuppressWarnings("SameReturnValue")
    private static String playstore(List<String> args, Context c) {
        String toSearch = Tuils.toPlanString(args, "%20");

        try {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAYSTORE_PREFIX + toSearch)));
        } catch (android.content.ActivityNotFoundException anfe) {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAYSTORE_BROWSER_PREFIX + toSearch)));
        }

        return Tuils.EMPTYSTRING;
    }

    @SuppressWarnings("SameReturnValue")
    private static String url(String url, Context c) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        c.startActivity(intent);

        return Tuils.EMPTYSTRING;
    }

    @SuppressWarnings("SameReturnValue")
    private static String duckDuck(List<String> args, Context c) {
        try {
            String toSearch = Tuils.toPlanString(args, " ");

            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.setClassName(DUCKDUCKGO_PACKAGE, DUCKDUCKGO_PACKAGE + DUCKDUCKGO_ACTIVITY);
            intent.putExtra(SearchManager.QUERY, toSearch);
            c.startActivity(intent);
        } catch (Exception e) {
            String toSearch = Tuils.toPlanString(args, "+");

            Uri uri = Uri.parse(DUCKDUCKGO_PREFIX + toSearch);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            c.startActivity(intent);
        }
        return Tuils.EMPTYSTRING;
    }

    @SuppressWarnings("SameReturnValue")
    private static String youTube(List<String> args, Context c) {
        try {
            String toSearch = Tuils.toPlanString(args, " ");
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            intent.setPackage(YOUTUBE_PACKAGE);
            intent.putExtra("query", toSearch);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
        } catch (Exception e) {
            String toSearch = Tuils.toPlanString(args, "+");
            Uri uri = Uri.parse(YOUTUBE_PREFIX + toSearch);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            c.startActivity(intent);
        }
        return Tuils.EMPTYSTRING;
    }

    @Override
    public int helpRes() {
        return R.string.help_search;
    }

    @Override
    public int priority() {
        return 4;
    }
}
