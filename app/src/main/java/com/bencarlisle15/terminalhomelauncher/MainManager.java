package com.bencarlisle15.terminalhomelauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Parcelable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bencarlisle15.terminalhomelauncher.commands.Command;
import com.bencarlisle15.terminalhomelauncher.commands.CommandGroup;
import com.bencarlisle15.terminalhomelauncher.commands.CommandTuils;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.commands.main.raw.location;
import com.bencarlisle15.terminalhomelauncher.commands.main.specific.RedirectCommand;
import com.bencarlisle15.terminalhomelauncher.managers.AliasManager;
import com.bencarlisle15.terminalhomelauncher.managers.AppsManager;
import com.bencarlisle15.terminalhomelauncher.managers.ChangelogManager;
import com.bencarlisle15.terminalhomelauncher.managers.ContactManager;
import com.bencarlisle15.terminalhomelauncher.managers.HTMLExtractManager;
import com.bencarlisle15.terminalhomelauncher.managers.MessagesManager;
import com.bencarlisle15.terminalhomelauncher.managers.RssManager;
import com.bencarlisle15.terminalhomelauncher.managers.TerminalManager;
import com.bencarlisle15.terminalhomelauncher.managers.ThemeManager;
import com.bencarlisle15.terminalhomelauncher.managers.TimeManager;
import com.bencarlisle15.terminalhomelauncher.managers.TuiLocationManager;
import com.bencarlisle15.terminalhomelauncher.managers.music.MusicManager2;
import com.bencarlisle15.terminalhomelauncher.managers.music.MusicService;
import com.bencarlisle15.terminalhomelauncher.managers.notifications.KeeperService;
import com.bencarlisle15.terminalhomelauncher.managers.xml.XMLPrefsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Behavior;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Theme;
import com.bencarlisle15.terminalhomelauncher.tuils.PrivateIOReceiver;
import com.bencarlisle15.terminalhomelauncher.tuils.StoppableThread;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;
import com.bencarlisle15.terminalhomelauncher.tuils.interfaces.CommandExecuter;
import com.bencarlisle15.terminalhomelauncher.tuils.interfaces.OnRedirectionListener;
import com.bencarlisle15.terminalhomelauncher.tuils.interfaces.Redirectator;
import com.bencarlisle15.terminalhomelauncher.tuils.libsuperuser.Shell;
import com.bencarlisle15.terminalhomelauncher.tuils.libsuperuser.ShellHolder;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/*Copyright Francesco Andreuzzi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

public class MainManager {

    public static final String ACTION_EXEC = BuildConfig.APPLICATION_ID + ".main_exec";
    public static final String CMD = "cmd";
    public static final String NEED_WRITE_INPUT = "writeInput";
    public static final String ALIAS_NAME = "aliasName";
    public static final String PARCELABLE = "parcelable";
    public static final String CMD_COUNT = "cmdCount";
    public static final String MUSIC_SERVICE = "musicService";

    private RedirectCommand redirect;
    private final Redirectator redirectator = new Redirectator() {
        @Override
        public void prepareRedirection(RedirectCommand cmd) {
            redirect = cmd;

            if(redirectionListener != null) {
                redirectionListener.onRedirectionRequest(cmd);
            }
        }

        @Override
        public void cleanup() {
            if(redirect != null) {
                redirect.beforeObjects.clear();
                redirect.afterObjects.clear();

                if(redirectionListener != null) {
                    redirectionListener.onRedirectionEnd(redirect);
                }

                redirect = null;
            }
        }
    };
    private OnRedirectionListener redirectionListener;
    public void setRedirectionListener(OnRedirectionListener redirectionListener) {
        this.redirectionListener = redirectionListener;
    }

    private final String COMMANDS_PKG = "com.bencarlisle15.terminalhomelauncher.commands.main.raw";

    private final CmdTrigger[] triggers = new CmdTrigger[] {
            new GroupTrigger(),
            new AliasTrigger(),
            new TuiCommandTrigger(),
            new AppTrigger(),
            new ShellCommandTrigger()
    };
    private final MainPack mainPack;

    private final LauncherActivity mContext;

    private final boolean showAliasValue;
    private final boolean showAppHistory;
    private final int aliasContentColor;

    private final String multipleCmdSeparator;

    public static Shell.Interactive interactive;

    private final AliasManager aliasManager;
    private final RssManager rssManager;
    private final AppsManager appsManager;
    private ContactManager contactManager;
    private final MusicManager2 musicManager2;
    private final ThemeManager themeManager;
    private final HTMLExtractManager htmlExtractManager;

    MessagesManager messagesManager;

    private final BroadcastReceiver receiver;

    public static int commandCount = 0;

    private final boolean keeperServiceRunning;

    protected MainManager(LauncherActivity c) {
        mContext = c;

        keeperServiceRunning = XMLPrefsManager.getBoolean(Behavior.tui_notification);

        showAliasValue = XMLPrefsManager.getBoolean(Behavior.show_alias_content);
        showAppHistory = XMLPrefsManager.getBoolean(Behavior.show_launch_history);
        aliasContentColor = XMLPrefsManager.getColor(Theme.alias_content_color);

        multipleCmdSeparator = XMLPrefsManager.get(Behavior.multiple_cmd_separator);

        CommandGroup group = new CommandGroup(mContext, COMMANDS_PKG);

        try {
            contactManager = new ContactManager(mContext);
        } catch (NullPointerException e) {
            Tuils.log(e);
        }

        appsManager = new AppsManager(c);
        aliasManager = new AliasManager(mContext);

        final OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(mContext.getCacheDir(), 10*1024*1024))
                .build();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                int lat = -90, lon = 0;
//
//                for(int j = 0; j < 120; j++) {
//                    Tuils.log("----------------" + j + "----------------");
//
//                    try {
//                        Request.Builder builder = new Request.Builder()
//                                .url("http://api.openweathermap.org/data/2.5/weather?lat=" + lat++ + "&lon=" + lon++ + "&appid=1f798f99228596c20ccfda51b9771a86&units=metric")
//                                .cacheControl(CacheControl.FORCE_NETWORK)
//                                .get();
//
//                        Response response = client.newCall(builder.build()).execute();
//
//                        Tuils.log("code", response.code());
//                        if (!response.isSuccessful()) {
//                            Tuils.log("not succesfull");
//                            return;
//                        }
//
//                        InputStream inputStream = response.body().byteStream();
//                        String json = Tuils.inputStreamToString(inputStream);
//                        Tuils.log(json);
//                    } catch (Exception e) {
//                        Tuils.log(e);
//                    }
//                }
//            }
//        }.start();

        rssManager = new RssManager(mContext, client);
        themeManager = new ThemeManager(client, mContext, c);
        musicManager2 = XMLPrefsManager.getBoolean(Behavior.enable_music) ? new MusicManager2(mContext) : null;
        ChangelogManager.printLog(mContext, client);
        htmlExtractManager = new HTMLExtractManager(mContext, client);

        if(XMLPrefsManager.getBoolean(Behavior.show_hints)) {
            messagesManager = new MessagesManager(mContext);
        }

        mainPack = new MainPack(mContext, group, aliasManager, appsManager, musicManager2, contactManager, redirectator, rssManager, client);

        ShellHolder shellHolder = new ShellHolder(mContext);
        interactive = shellHolder.build();
        mainPack.shellHolder = shellHolder;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_EXEC);
        filter.addAction(location.ACTION_LOCATION_CMD_GOT);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ACTION_EXEC)) {
                    String cmd = intent.getStringExtra(CMD);
                    if (cmd == null) cmd = intent.getStringExtra(PrivateIOReceiver.TEXT);

                    if (cmd == null) {
                        return;
                    }

                    int cmdCount = intent.getIntExtra(CMD_COUNT, -1);
                    if (cmdCount < commandCount) return;
                    commandCount++;

                    String aliasName = intent.getStringExtra(ALIAS_NAME);
                    boolean needWriteInput = intent.getBooleanExtra(NEED_WRITE_INPUT, false);
                    Parcelable p = intent.getParcelableExtra(PARCELABLE);

                    if(needWriteInput) {
                        Intent i = new Intent(PrivateIOReceiver.ACTION_INPUT);
                        i.putExtra(PrivateIOReceiver.TEXT, cmd);
                        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(i);
                    }

                    if(p instanceof AppsManager.LaunchInfo) {
                        onCommand(cmd, (AppsManager.LaunchInfo) p, intent.getBooleanExtra(MainManager.MUSIC_SERVICE, false));
                    } else {
                        onCommand(cmd, aliasName, intent.getBooleanExtra(MainManager.MUSIC_SERVICE, false));
                    }
                } else if(action.equals(location.ACTION_LOCATION_CMD_GOT)) {
                    Tuils.sendOutput(context, "Lat: " + intent.getDoubleExtra(TuiLocationManager.LATITUDE, 0) + "; Long: " + intent.getDoubleExtra(TuiLocationManager.LONGITUDE, 0));
                    TuiLocationManager.instance(context).rm(location.ACTION_LOCATION_CMD_GOT);
                }
            }
        };

        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).registerReceiver(receiver, filter);
    }

    private void updateServices(String cmd, boolean wasMusicService) {

        if(keeperServiceRunning) {
            Intent i = new Intent(mContext, KeeperService.class);
            i.putExtra(KeeperService.CMD_KEY, cmd);
            i.putExtra(KeeperService.PATH_KEY, mainPack.currentDirectory.getAbsolutePath());
            mContext.startService(i);
        }

        if(wasMusicService) {
            Intent i = new Intent(mContext, MusicService.class);
            mContext.startService(i);
        }
    }

    public void onCommand(String input, AppsManager.LaunchInfo launchInfo, boolean wasMusicService) {
        if(launchInfo == null) {
            onCommand(input, (String) null, wasMusicService);
            return;
        }

        updateServices(input, wasMusicService);

        if(launchInfo.unspacedLowercaseLabel.equals(Tuils.removeSpaces(input.toLowerCase()))) {
            performLaunch(mainPack, launchInfo, input);
        } else {
            onCommand(input, (String) null, wasMusicService);
        }
    }

    final Pattern colorExtractor = Pattern.compile("(#[^(]{6})\\[([^\\)]*)\\]", Pattern.CASE_INSENSITIVE);

//    command manager
    public void onCommand(String input, String alias, boolean wasMusicService) {
        input = Tuils.removeUnncesarySpaces(input);

        if(alias == null) updateServices(input, wasMusicService);

        if(redirect != null) {
            if(!redirect.isWaitingPermission()) {
                redirect.afterObjects.add(input);
            }
            String output = redirect.onRedirect(mainPack);
            Tuils.sendOutput(mContext, output);

            return;
        }

        if(alias != null && showAliasValue) {
           Tuils.sendOutput(aliasContentColor, mContext, aliasManager.formatLabel(alias, input));
        }

        String[] cmds;
        if(multipleCmdSeparator.length() > 0) {
            cmds = input.split(multipleCmdSeparator);
        } else {
            cmds = new String[] {input};
        }

        int[] colors = new int[cmds.length];
        for(int c = 0; c < colors.length; c++) {
            Matcher m = colorExtractor.matcher(cmds[c]);
            if(m.matches()) {
                try {
                    colors[c] = Color.parseColor(m.group(1));
                    cmds[c] = m.group(2);
                } catch (Exception e) {
                    colors[c] = TerminalManager.NO_COLOR;
                }
            } else colors[c] = TerminalManager.NO_COLOR;
        }

        for(int c = 0; c < cmds.length; c++) {
            mainPack.clear();
            mainPack.commandColor = colors[c];

            for (CmdTrigger trigger : triggers) {
                boolean r;
                try {
                    r = trigger.trigger(mainPack, cmds[c]);
                } catch (Exception e) {
                    Tuils.sendOutput(mContext, Tuils.getStackTrace(e));
                    break;
                }
                if (r) {
                    if(messagesManager != null) messagesManager.afterCmd();
                    break;
                }
            }
        }
    }

    public void onLongBack() {
        Tuils.sendInput(mContext, Tuils.EMPTYSTRING);
    }

    public void sendPermissionNotGrantedWarning() {
        redirectator.cleanup();
    }

    public void dispose() {
        mainPack.dispose();
    }

    public void destroy() {
        mainPack.destroy();
        TuiLocationManager.disposeStatic();

        if(messagesManager != null) messagesManager.onDestroy();

        themeManager.dispose();
        htmlExtractManager.dispose(mContext);
        aliasManager.dispose();
        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).unregisterReceiver(receiver);

        new StoppableThread() {
            @Override
            public void run() {
                super.run();

                try {
                    interactive.kill();
                    interactive.close();
                } catch (Exception e) {
                    Tuils.log(e);
                    Tuils.toFile(e);
                }
            }
        }.start();
    }

    public MainPack getMainPack() {
        return mainPack;
    }

    public CommandExecuter executer() {
        return (input, obj) -> {
            AppsManager.LaunchInfo li = obj instanceof AppsManager.LaunchInfo ? (AppsManager.LaunchInfo) obj : null;

            onCommand(input, li, false);
        };
    }

//
    String appFormat;
    int outputColor;

    final Pattern pa = Pattern.compile("%a", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    final Pattern pp = Pattern.compile("%p", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    final Pattern pl = Pattern.compile("%l", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);

    public boolean performLaunch(MainPack mainPack, AppsManager.LaunchInfo i, String input) {
        Intent intent = appsManager.getIntent(i);
        if (intent == null) {
            return false;
        }

        if(showAppHistory) {
            if(appFormat == null) {
                appFormat = XMLPrefsManager.get(Behavior.app_launch_format);
                outputColor = XMLPrefsManager.getColor(Theme.output_color);
            }

            String a = appFormat;
            a = pa.matcher(a).replaceAll(Matcher.quoteReplacement(intent.getComponent().getClassName()));
            a = pp.matcher(a).replaceAll(Matcher.quoteReplacement(intent.getComponent().getPackageName()));
            a = pl.matcher(a).replaceAll(Matcher.quoteReplacement(i.publicLabel));
            a = Tuils.patternNewline.matcher(a).replaceAll(Matcher.quoteReplacement(Tuils.NEWLINE));

            SpannableString text = new SpannableString(a);
            text.setSpan(new ForegroundColorSpan(outputColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            CharSequence s = TimeManager.instance.replace(text);

            Tuils.sendOutput(mainPack, s, TerminalManager.CATEGORY_OUTPUT);
        }

        mainPack.context.startActivity(intent);

        return true;
    }
//

    public interface CmdTrigger {
        boolean trigger(MainPack info, String input) throws Exception;
    }

    private class AliasTrigger implements CmdTrigger {

        @Override
        public boolean trigger(MainPack info, String input) {
            String[] alias = aliasManager.getAlias(input, true);

            String aliasValue = alias[0];
            if (alias[0] == null) {
                return false;
            }

            String aliasName = alias[1];
            String residual = alias[2];

            aliasValue = aliasManager.format(aliasValue, residual);

            onCommand(aliasValue, aliasName, false);

            return true;
        }
    }

    private class GroupTrigger implements CmdTrigger {

        @Override
        public boolean trigger(MainPack info, String input) throws Exception {
            int index = input.indexOf(Tuils.SPACE);
            String name;

            if(index != -1) {
                name = input.substring(0,index);
                input = input.substring(index + 1);
            } else {
                name = input;
                input = null;
            }

            List<? extends Group> appGroups = info.appsManager.groups;
            if(appGroups != null) {
                for(Group g : appGroups) {
                    if(name.equals(g.name())) {
                        if(input == null) {
                            Tuils.sendOutput(mContext, AppsManager.AppUtils.printApps(AppsManager.AppUtils.labelList((List<AppsManager.LaunchInfo>) g.members(), false)));
                            return true;
                        } else {
                            return g.use(mainPack, input);
                        }
                    }
                }
            }

            return false;
        }
    }

    private class ShellCommandTrigger implements CmdTrigger {

        final int CD_CODE = 10;
        final int PWD_CODE = 11;

        final Shell.OnCommandResultListener result = new Shell.OnCommandResultListener() {
            @Override
            public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                if(commandCode == CD_CODE) {
                    interactive.addCommand("pwd", PWD_CODE, result);
                } else if(commandCode == PWD_CODE && output.size() == 1) {
                    File f = new File(output.get(0));
                    if(f.exists()) {
                        mainPack.currentDirectory = f;

                        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(new Intent(UIManager.ACTION_UPDATE_HINT));
                    }
                }
            }
        };

        @Override
        public boolean trigger(final MainPack info, final String input) throws Exception {
            new StoppableThread() {
                @Override
                public void run() {
                    if(input.trim().equalsIgnoreCase("su")) {
                        if(Shell.SU.available()) LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(new Intent(UIManager.ACTION_ROOT));
                        interactive.addCommand("su");

                    } else if(input.contains("cd ")) {
                        interactive.addCommand(input, CD_CODE, result);
                    } else interactive.addCommand(input);

                }
            }.start();

            return true;
        }
    }

    private class AppTrigger implements CmdTrigger {

        @Override
        public boolean trigger(MainPack info, String input) {
            AppsManager.LaunchInfo i = appsManager.findLaunchInfoWithLabel(input, AppsManager.SHOWN_APPS);
            return i != null && performLaunch(info, i, input);
        }
    }

    private class TuiCommandTrigger implements CmdTrigger {

        @Override
        public boolean trigger(final MainPack info, final String input) throws Exception {

            final Command command = CommandTuils.parse(input, info);
            if(command == null) return false;

            mainPack.lastCommand = input;

            new StoppableThread() {
                @Override
                public void run() {
                    super.run();

                    try {
                        String output = command.exec(info);
                        if(output != null) {
                            Tuils.sendOutput(info, output, TerminalManager.CATEGORY_OUTPUT);
                        }
                    } catch (Exception e) {
                        Tuils.sendOutput(mContext, Tuils.getStackTrace(e));
                        Tuils.log(e);
                    }
                }
            }.start();

            return true;
        }
    }

    public interface Group {
        List<?> members();
        boolean use(MainPack mainPack, String input);
        String name();
    }
}
