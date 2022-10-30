package com.bencarlisle15.terminalhomelauncher.tuils;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bencarlisle15.terminalhomelauncher.BuildConfig;
import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.managers.TerminalManager;
import com.bencarlisle15.terminalhomelauncher.managers.music.MusicManager;
import com.bencarlisle15.terminalhomelauncher.managers.music.Song;
import com.bencarlisle15.terminalhomelauncher.managers.notifications.NotificationService;
import com.bencarlisle15.terminalhomelauncher.managers.xml.XMLPrefsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.classes.XMLPrefsSave;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Behavior;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Ui;
import com.bencarlisle15.terminalhomelauncher.tuils.interfaces.OnBatteryUpdate;
import com.bencarlisle15.terminalhomelauncher.tuils.stuff.FakeLauncherActivity;

import org.xml.sax.SAXParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dalvik.system.DexFile;

public class Tuils {

    public static final String SPACE = " ";
    public static final String DOUBLE_SPACE = "  ";
    public static final String NEWLINE = "\n";
    public static final String TRIBLE_SPACE = "   ";
    public static final String DOT = ".";
    public static final String EMPTYSTRING = "";
    public static final String MINUS = "-";
    public static final int TERA = 0;
    public static final int GIGA = 1;
    public static final int MEGA = 2;
    public static final int KILO = 3;
    public static final int BYTE = 4;
    private static final String TUI_FOLDER = "t-ui";
    private static final View.OnClickListener deepClickListener = v -> Tuils.log(v.toString());
    private static final long total = -1;
    private static final int FILEUPDATE_DELAY = 100;
    private static final String SPACE_REGEXP = "\\s";
    public static final Pattern patternNewline = Pattern.compile("%n", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    public static String fontPath = null;
    static final Pattern calculusPattern = Pattern.compile("([\\+\\-\\*\\/\\^])(\\d+\\.?\\d*)");
    static final Pattern pd = Pattern.compile("%d", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    static final Pattern pu = Pattern.compile("%u", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    static final Pattern pp = Pattern.compile("%p", Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    static final Pattern unnecessarySpaces = Pattern.compile("\\s{2,}");
    private static Typeface globalTypeface = null;
    private static OnBatteryUpdate batteryUpdate;
    private static BroadcastReceiver batteryReceiver = null;
    private static File folder = null;

    public static double textCalculus(double input, String text) {
        Matcher m = calculusPattern.matcher(text);
        while (m.find()) {
            char operator = Objects.requireNonNull(m.group(1)).charAt(0);
            double value = Double.parseDouble(Objects.requireNonNull(m.group(2)));

            switch (operator) {
                case '+':
                    input += value;
                    break;
                case '-':
                    input -= value;
                    break;
                case '*':
                    input *= value;
                    break;
                case '/':
                    input = input / value;
                    break;
                case '^':
                    input = Math.pow(input, value);
                    break;
            }

            Tuils.log("now im", input);
        }

        return input;
    }

    public static Typeface getTypeface(Context context) {
        if (globalTypeface == null) {
            try {
                XMLPrefsManager.loadCommons(context);
            } catch (Exception e) {
                return null;
            }

            boolean systemFont = XMLPrefsManager.getBoolean(Ui.system_font);
            if (systemFont) globalTypeface = Typeface.DEFAULT;
            else {
                File tui = Tuils.getFolder();
                if (tui == null) {
                    return Typeface.createFromAsset(context.getAssets(), "lucida_console.ttf");
                }

                Pattern p = Pattern.compile(".[ot]tf$");

                File font = null;
                for (File f : Objects.requireNonNull(tui.listFiles())) {
                    String name = f.getName();
                    if (p.matcher(name).find()) {
                        font = f;
                        fontPath = f.getAbsolutePath();
                        break;
                    }
                }

                if (font != null) {
                    try {
                        globalTypeface = Typeface.createFromFile(font);
                        if (globalTypeface == null) throw new UnsupportedOperationException();
                    } catch (Exception e) {
                        globalTypeface = null;
                    }
                }
            }

            if (globalTypeface == null)
                globalTypeface = systemFont ? Typeface.DEFAULT : Typeface.createFromAsset(context.getAssets(), "lucida_console.ttf");
        }
        return globalTypeface;
    }

    public static void cancelFont() {
        globalTypeface = null;
        fontPath = null;
    }

    public static String locationName(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getAddressLine(2);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean notificationServiceIsRunning(Context context) {
        ComponentName collectorComponent = new ComponentName(context, NotificationService.class);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) {
            return false;
        }

        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == Process.myPid()) {
                    collectorRunning = true;
                }
            }
        }

        return collectorRunning;
    }

    public static boolean arrayContains(int[] array, int value) {
        if (array == null) return false;

        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    public static void registerBatteryReceiver(Context context, OnBatteryUpdate listener) {
        try {
            batteryReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (batteryUpdate == null) return;

                    switch (intent.getAction()) {
                        case Intent.ACTION_BATTERY_CHANGED:
                            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                            batteryUpdate.update(level);
                            break;
                        case Intent.ACTION_POWER_CONNECTED:
                            batteryUpdate.onCharging();
                            break;
                        case Intent.ACTION_POWER_DISCONNECTED:
                            batteryUpdate.onNotCharging();
                            break;
                    }
                }
            };

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            iFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            iFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

            context.registerReceiver(batteryReceiver, iFilter);

            batteryUpdate = listener;
        } catch (Exception e) {
            Tuils.toFile(e);
        }
    }

    public static void unregisterBatteryReceiver(Context context) {
        if (batteryReceiver != null) context.unregisterReceiver(batteryReceiver);
    }

    public static boolean containsExtension(String[] array, String value) {
        try {
            value = value.toLowerCase().trim();
            for (String s : array) {
                if (value.endsWith(s)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<Song> getSongsInFolder(File folder) {
        List<Song> songs = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return songs;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                List<Song> s = getSongsInFolder(file);
                songs.addAll(s);
            } else if (containsExtension(MusicManager.MUSIC_EXTENSIONS, file.getName())) {
                songs.add(new Song(file));
            }
        }

        return songs;
    }

    public static long download(InputStream in, File file) throws Exception {
        OutputStream out = new FileOutputStream(file, false);

        byte[] data = new byte[1024];

        long bytes = 0;

        int count;
        while ((count = in.read(data)) != -1) {
            out.write(data, 0, count);
            bytes += count;
        }

        out.flush();
        out.close();
        in.close();

        return bytes;
    }

    public static void write(File file, String separator, String... ss) throws Exception {
        FileOutputStream headerStream = new FileOutputStream(file, false);

        for (int c = 0; c < ss.length - 1; c++) {
            headerStream.write(ss[c].getBytes());
            headerStream.write(separator.getBytes());
        }
        headerStream.write(ss[ss.length - 1].getBytes());

        headerStream.flush();
        headerStream.close();
    }

    public static boolean hasNotificationAccess(Context context) {
        String pkgName = BuildConfig.APPLICATION_ID;
        final String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void resetPreferredLauncherAndOpenChooser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, FakeLauncherActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    public static void openSettingsPage(Context c, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        c.startActivity(intent);
    }

    public static Intent requestAdmin(ComponentName component, String explanation) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, explanation);
        return intent;
    }

    public static Intent webPage(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    public static double getAvailableInternalMemorySize(int unit) {
        return getAvailableSpace(Environment.getDataDirectory(), unit);
    }

    public static double getTotalInternalMemorySize(int unit) {
        return getTotalSpace(Environment.getDataDirectory(), unit);
    }

    public static double getAvailableExternalMemorySize(int unit) {
        try {
            return getAvailableSpace(XMLPrefsManager.get(File.class, Behavior.external_storage_path), unit);
        } catch (Exception e) {
            return -1;
        }
    }

    public static double getTotalExternalMemorySize(int unit) {
        try {
            return getTotalSpace(XMLPrefsManager.get(File.class, Behavior.external_storage_path), unit);
        } catch (Exception e) {
            return -1;
        }
    }

    public static double getAvailableSpace(File dir, int unit) {
        if (dir == null) return -1;

        StatFs statFs = new StatFs(dir.getAbsolutePath());
        long blocks = statFs.getAvailableBlocksLong();

        return formatSize(blocks * statFs.getBlockSizeLong(), unit);
    }

    public static double getTotalSpace(File dir, int unit) {
        if (dir == null) return -1;

        StatFs statFs = new StatFs(dir.getAbsolutePath());
        long blocks = statFs.getBlockCountLong();
        return formatSize(blocks * statFs.getBlockSizeLong(), unit);
    }

    public static double percentage(double part, double total) {
        return round(part * 100 / total, 2);
    }

    public static double formatSize(long bytes, int unit) {
        double convert = 1048576.0;
        double smallConvert = 1024.0;

        double result;

        switch (unit) {
            case TERA:
                result = (bytes / convert) / convert;
                break;
            case GIGA:
                result = (bytes / convert) / smallConvert;
                break;
            case MEGA:
                result = bytes / convert;
                break;
            case KILO:
                result = bytes / smallConvert;
                break;
            case BYTE:
                result = bytes;
                break;
            default:
                return -1;
        }

        return round(result, 2);
    }

    public static boolean isMyLauncherDefault(PackageManager packageManager) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<>();
        filters.add(filter);

        final String myPackageName = BuildConfig.APPLICATION_ID;
        List<ComponentName> activities = new ArrayList<>();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static SpannableString span(CharSequence text, int color) {
        return span(null, text, color, Integer.MAX_VALUE);
    }

    public static SpannableString span(Context context, int size, CharSequence text) {
        return span(context, text, Integer.MAX_VALUE, size);
    }

    public static SpannableString span(Context context, CharSequence text, int color, int size) {
        return span(context, Integer.MAX_VALUE, color, text, size);
    }

    public static SpannableString span(int bgColor, int foreColor, CharSequence text) {
        return span(null, bgColor, foreColor, text, Integer.MAX_VALUE);
    }

    public static SpannableString span(Context context, int bgColor, int foreColor, CharSequence text, int size) {
        if (text == null) {
            text = Tuils.EMPTYSTRING;
        }

        SpannableString spannableString;
        if (text instanceof SpannableString) spannableString = (SpannableString) text;
        else spannableString = new SpannableString(text);

        if (size != Integer.MAX_VALUE && context != null)
            spannableString.setSpan(new AbsoluteSizeSpan(convertSpToPixels(size, context)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (foreColor != Integer.MAX_VALUE)
            spannableString.setSpan(new ForegroundColorSpan(foreColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (bgColor != Integer.MAX_VALUE)
            spannableString.setSpan(new BackgroundColorSpan(bgColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static int span(int bgColor, SpannableString text, String section, int fromIndex) {
        int index = text.toString().indexOf(section, fromIndex);
        if (index == -1) return index;

        text.setSpan(new BackgroundColorSpan(bgColor), index, index + section.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return index + section.length();
    }

    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static String inputStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : Tuils.EMPTYSTRING;
    }

    public static void deleteContentOnly(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                delete(f);
            }
            if (!f.delete()) {
                Tuils.log("Could not delete dir at " + f.getAbsolutePath());
            }
        }
    }

    public static void delete(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                delete(f);
            }
            if (!f.delete()) {
                Tuils.log("Could not delete file at " + f.getAbsolutePath());
            }
        }
        if (!dir.delete()) {
            Tuils.log("Could not delete dir at " + dir.getAbsolutePath());
        }
    }

    public static void insertOld(File oldFile) {
        if (oldFile == null || !oldFile.exists()) return;

        String oldPath = oldFile.getAbsolutePath();

        File oldFolder = new File(Tuils.getFolder(), "old");
        if (!oldFolder.exists() && !oldFolder.mkdir()) {
            Tuils.log("Could not mkdir at " + oldFolder.getAbsolutePath());
        }

        File dest = new File(oldFolder, oldFile.getName());
        if (dest.exists() && !dest.delete()) {
            Tuils.log("Could not delete file at " + dest.getAbsolutePath());
        }

        if (oldFile.renameTo(dest) && !new File(oldPath).delete()) {
            Tuils.log("Could not delete file at " + oldPath);
        }
    }

    public static File getOld(String name) {
        File old = new File(Tuils.getFolder(), "old");
        File file = new File(old, name);

        if (file.exists()) return file;
        return null;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void sendOutput(Context context, int res) {
        sendOutput(Integer.MAX_VALUE, context, res);
    }

    public static void sendOutput(int color, Context context, int res) {
        sendOutput(color, context, context.getString(res));
    }

    public static void sendOutput(Context context, int res, int type) {
        sendOutput(Integer.MAX_VALUE, context, res, type);
    }

    public static void sendOutput(int color, Context context, int res, int type) {
        sendOutput(color, context, context.getString(res), type);
    }

    public static void sendOutput(Context context, CharSequence s) {
        sendOutput(Integer.MAX_VALUE, context, s);
    }

    public static void sendOutput(int color, Context context, CharSequence s) {
        sendOutput(color, context, s, TerminalManager.CATEGORY_OUTPUT);
    }

    public static void sendOutput(Context context, CharSequence s, int type) {
        sendOutput(Integer.MAX_VALUE, context, s, type);
    }

    public static void sendOutput(int color, Context context, CharSequence s, int type) {
        Intent intent = new Intent(PrivateIOReceiver.ACTION_OUTPUT);
        intent.putExtra(PrivateIOReceiver.TEXT, s);
        intent.putExtra(PrivateIOReceiver.COLOR, color);
        intent.putExtra(PrivateIOReceiver.TYPE, type);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendOutput(MainPack mainPack, CharSequence s, int type) {
        sendOutput(mainPack.commandColor, mainPack.context, s, type);
    }

    public static void sendOutput(Context context, CharSequence s, int type, Object action) {
        sendOutput(Integer.MAX_VALUE, context, s, type, action);
    }

    public static void sendOutput(int color, Context context, CharSequence s, int type, Object action) {
        Intent intent = new Intent(PrivateIOReceiver.ACTION_OUTPUT);
        intent.putExtra(PrivateIOReceiver.TEXT, s);
        intent.putExtra(PrivateIOReceiver.COLOR, color);
        intent.putExtra(PrivateIOReceiver.TYPE, type);

        if (action instanceof String) {
            intent.putExtra(PrivateIOReceiver.ACTION, (String) action);
        } else if (action instanceof Parcelable) {
            intent.putExtra(PrivateIOReceiver.ACTION_PARSEABLE, (Parcelable) action);
        }

        Log.e("SENDING", action + " " + intent.getExtras().toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendOutput(Context context, CharSequence s, int type, Object action, Object longAction) {
        sendOutput(Integer.MAX_VALUE, context, s, type, action, longAction);
    }

    public static void sendOutput(int color, Context context, CharSequence s, int type, Object action, Object longAction) {
        Intent intent = new Intent(PrivateIOReceiver.ACTION_OUTPUT);
        intent.putExtra(PrivateIOReceiver.TEXT, s);
        intent.putExtra(PrivateIOReceiver.COLOR, color);
        intent.putExtra(PrivateIOReceiver.TYPE, type);

        if (action instanceof String) {
            intent.putExtra(PrivateIOReceiver.ACTION, (String) action);
        } else if (action instanceof Parcelable) {
            intent.putExtra(PrivateIOReceiver.ACTION_PARSEABLE, (Parcelable) action);
        }

        if (longAction instanceof String)
            intent.putExtra(PrivateIOReceiver.LONG_ACTION, (String) longAction);
        else if (longAction instanceof Parcelable)
            intent.putExtra(PrivateIOReceiver.LONG_ACTION_PARSEABLE, (Parcelable) longAction);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendInput(Context context, String text) {
        Intent intent = new Intent(PrivateIOReceiver.ACTION_INPUT);
        intent.putExtra(PrivateIOReceiver.TEXT, text);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static double freeRam(ActivityManager mgr, MemoryInfo info) {
        mgr.getMemoryInfo(info);
        return info.availMem;
    }

    public static long totalRam() {
        if (total > 0) return total;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/meminfo")));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("MemTotal")) {
                    line = line.replaceAll("\\D+", Tuils.EMPTYSTRING);
                    return Long.parseLong(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double round(double value, int places) {
        if (places < 0) places = 0;

        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e) {
            return value;
        }
    }

    public static List<String> getClassesInPackage(String packageName, Context c) throws IOException {
        List<String> classes = new ArrayList<>();
        String packageCodePath = c.getPackageCodePath();
        DexFile df = new DexFile(packageCodePath);
        return Collections.list(df.entries()).stream().filter(className -> className.contains(packageName) && !className.contains("$")).collect(Collectors.toList());
    }

    public static int scale(int[] from, int[] to, int n) {
        return (to[1] - to[0]) * (n - from[0]) / (from[1] - from[0]) + to[0];
    }

    public static String[] toString(Enum<?>[] enums) {
        String[] arr = new String[enums.length];
        for (int count = 0; count < enums.length; count++) arr[count] = enums[count].name();
        return arr;
    }

    private static String getNicePath(String filePath) {
        if (filePath == null) return "null";

        String home = XMLPrefsManager.get(File.class, Behavior.home_path).getAbsolutePath();

        if (filePath.equals(home)) {
            return "~";
        } else if (filePath.startsWith(home)) {
            return "~" + filePath.replace(home, Tuils.EMPTYSTRING);
        } else {
            return filePath;
        }
    }

    public static int find(Object o, Object[] array) {
        return find(o, Arrays.asList(array));
    }

    public static int find(Object o, List<?> list) {
        for (int count = 0; count < list.size(); count++) {
            Object x = list.get(count);
            if (x == null) continue;

            if (o == x) return count;

            if (o instanceof XMLPrefsSave) {
                try {
                    if (((XMLPrefsSave) o).label().equals(x)) return count;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (o instanceof String && x instanceof XMLPrefsSave) {
                try {
                    if (((XMLPrefsSave) x).label().equals(o)) return count;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                if (o.equals(x) || x.equals(o)) return count;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static String getHint(String currentPath) {
        if (!XMLPrefsManager.getBoolean(Ui.show_session_info)) return null;

        String format = XMLPrefsManager.get(Behavior.session_info_format);
        if (format.length() == 0) return null;

        String deviceName = XMLPrefsManager.get(Ui.deviceName);
        if (deviceName == null || deviceName.length() == 0) {
            deviceName = Build.DEVICE;
        }

        String username = XMLPrefsManager.get(Ui.username);
        if (username == null) username = Tuils.EMPTYSTRING;

        format = pd.matcher(format).replaceAll(Matcher.quoteReplacement(deviceName));
        format = pu.matcher(format).replaceAll(Matcher.quoteReplacement(username));
        format = pp.matcher(format).replaceAll(Matcher.quoteReplacement(Tuils.getNicePath(currentPath)));

        return format;
    }

    public static int mmToPx(DisplayMetrics metrics, int mm) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, metrics);
    }

    public static void insertHeaders(List<String> s, boolean newLine) {
        char current = 0;
        for (int count = 0; count < s.size(); count++) {
            String st = s.get(count).trim().toUpperCase();
            char c = st.charAt(0);
            if (current != c) {
                s.add(count, (newLine ? NEWLINE : EMPTYSTRING) + c + (newLine ? NEWLINE : EMPTYSTRING));
                current = c;
            }
        }
    }

    public static void addPrefix(List<String> list, String prefix) {
        for (int count = 0; count < list.size(); count++) {
            list.set(count, prefix.concat(list.get(count)));
        }
    }

    public static void addSeparator(List<String> list, String separator) {
        for (int count = 0; count < list.size(); count++)
            list.set(count, list.get(count).concat(separator));
    }

    public static String toPlanString(String[] strings, String separator) {
        if (strings == null) {
            return Tuils.EMPTYSTRING;
        }

        String output = Tuils.EMPTYSTRING;
        for (int count = 0; count < strings.length; count++) {
            output = output.concat(strings[count]);
            if (count < strings.length - 1) output = output.concat(separator);
        }
        return output;
    }

    public static String toPlanString(String[] strings) {
        if (strings != null) {
            return Tuils.toPlanString(strings, Tuils.NEWLINE);
        }
        return Tuils.EMPTYSTRING;
    }

    public static String toPlanString(String separator, List<?> strings) {
        if (strings == null) {
            return Tuils.EMPTYSTRING;
        }

        String output = Tuils.EMPTYSTRING;
        for (int count = 0; count < strings.size(); count++) {
            output = output.concat(strings.get(count).toString());
            if (count < strings.size() - 1) output = output.concat(separator);
        }
        return output;
    }

    public static void log(Object o) {
        if (o instanceof Throwable) {
            Log.e("bencarlisle15", "", (Throwable) o);
        } else {
            String text;
            if (o instanceof Object[]) text = Arrays.toString((Object[]) o);
            else text = o.toString();
            Log.e("bencarlisle15", text);
        }
    }

    public static void log(Object o, Object o2) {
        if (o instanceof Object[] && o2 instanceof Object[]) {
            Log.e("bencarlisle15", Arrays.toString((Object[]) o) + " -- " + Arrays.toString((Object[]) o2));
        } else {
            Log.e("bencarlisle15", o + " -- " + o2);
        }
    }

    public static void log(Object o, PrintStream to) {
//        Log.e("bencarlisle15", Arrays.toString(Thread.currentThread().getStackTrace()));

        if (o instanceof Throwable) {
            ((Throwable) o).printStackTrace(to);
        } else {
            String text;
            if (o instanceof Object[]) text = Arrays.toString((Object[]) o);
            else text = o.toString();

            try {
                to.write(text.getBytes());
            } catch (IOException e) {
                Tuils.log(e);
            }
        }
    }

    public static void log(Object o, Object o2, OutputStream to) {
        try {
            if (o instanceof Object[] && o2 instanceof Object[]) {
                to.write((Arrays.toString((Object[]) o) + " -- " + Arrays.toString((Object[]) o2)).getBytes());
            } else {
                to.write((o + " -- " + o2).getBytes());
            }
        } catch (Exception e) {
            Tuils.log(e);
        }
    }

    public static boolean hasNoInternetAccess() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
            return (urlc.getResponseCode() != 204 || urlc.getContentLength() != 0);
        } catch (IOException e) {
            return true;
        }
    }

    public static <T> T getDefaultValue(Class<T> clazz) {
        return (T) Array.get(Array.newInstance(clazz, 1), 0);
    }

    public static void toFile(String s) {
        try {
            RandomAccessFile f = new RandomAccessFile(new File(Tuils.getFolder(), "crash.txt"), "rw");
            f.seek(0);
            f.write((new Date() + Tuils.NEWLINE + Tuils.NEWLINE).getBytes());
            OutputStream is = Channels.newOutputStream(f.getChannel());
            is.write(s.getBytes());
            f.write((Tuils.NEWLINE + Tuils.NEWLINE).getBytes());

            is.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toFile(Object o) {
        if (o == null) return;

        try {
            FileOutputStream stream = new FileOutputStream(new File(Tuils.getFolder(), "crash.txt"));
            stream.write((Tuils.NEWLINE + Tuils.NEWLINE).getBytes());

            if (o instanceof Throwable) {
                PrintStream ps = new PrintStream(stream);
                ((Throwable) o).printStackTrace(ps);
            } else {
                stream.write(o.toString().getBytes());
            }

            stream.write((Tuils.NEWLINE + "----------------------------").getBytes());

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toPlanString(List<String> strings, String separator) {
        if (strings != null) {
            String[] object = new String[strings.size()];
            return Tuils.toPlanString(strings.toArray(object), separator);
        }
        return Tuils.EMPTYSTRING;
    }

    public static String toPlanString(List<String> strings) {
        return Tuils.toPlanString(strings, NEWLINE);
    }

    public static String toPlanString(Object[] objs, String separator) {
        if (objs == null) {
            return Tuils.EMPTYSTRING;
        }

        StringBuilder output = new StringBuilder();
        for (int count = 0; count < objs.length; count++) {
            output.append(objs[count]);
            if (count < objs.length - 1) {
                output.append(separator);
            }
        }
        return output.toString();
    }

    public static String removeUnncesarySpaces(String string) {
        return unnecessarySpaces.matcher(string).replaceAll(Tuils.SPACE);
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static boolean isAlpha(String s) {
        if (s == null) {
            return false;
        }
        char[] chars = s.toCharArray();

        for (char c : chars)
            if (!Character.isLetter(c))
                return false;

        return true;
    }

    public static boolean isPhoneNumber(String s) {
        if (s == null) {
            return false;
        }
        char[] chars = s.toCharArray();

        for (char c : chars) {
            if (Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    //    return 0 if only digit
    public static char firstNonDigit(String s) {
        if (s == null) {
            return 0;
        }

        char[] chars = s.toCharArray();

        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return c;
            }
        }

        return 0;
    }

    public static boolean isNumber(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        char[] chars = s.toCharArray();

        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static Intent openFile(Context c, File f) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri u = buildFile(c, f);
        String mimetype = MimeTypes.getMimeType(f.getAbsolutePath(), f.isDirectory());

        intent.setDataAndType(u, mimetype);

        int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION;

        intent.addFlags(flags);

        return intent;
    }

    public static Intent shareFile(Context c, File f) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri u = buildFile(c, f);

        String mimetype = MimeTypes.getMimeType(f.getAbsolutePath(), f.isDirectory());

        intent.setDataAndType(u, mimetype);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, u);

        return intent;
    }

    private static Uri buildFile(Context context, File file) {
        return FileProvider.getUriForFile(context, GenericFileProvider.PROVIDER_NAME, file);
    }

    private static File getTuiFolder(Context context) {
        File internalDir = context.getFilesDir();
        return new File(internalDir, TUI_FOLDER);
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public static String getTextFromClipboard(Context context) {
        try {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = manager.getPrimaryClip().getItemAt(0);
            return item.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getFolder() {
        return folder;
    }

    public static void setFolder(Context context) {
        if (folder != null) return;

        int elapsedTime = 0;
        while (elapsedTime < 1000) {
            File tuiFolder = Tuils.getTuiFolder(context);
            if (tuiFolder.exists() && tuiFolder.isDirectory() || tuiFolder.mkdir()) {
                folder = tuiFolder;
                return;
            }

            try {
                Thread.sleep(FILEUPDATE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            elapsedTime += FILEUPDATE_DELAY;
        }
    }

    public static int alphabeticCompare(String s1, String s2) {
        String cmd1 = removeSpaces(s1).toLowerCase();
        String cmd2 = removeSpaces(s2).toLowerCase();

        for (int count = 0; count < cmd1.length() && count < cmd2.length(); count++) {
            char c1 = cmd1.charAt(count);
            char c2 = cmd2.charAt(count);

            if (c1 < c2) {
                return -1;
            } else if (c1 > c2) {
                return 1;
            }
        }

        if (s1.length() > s2.length()) {
            return 1;
        } else if (s1.length() < s2.length()) {
            return -1;
        }
        return 0;
    }

    public static String removeSpaces(String string) {
        return string.replaceAll(SPACE_REGEXP, EMPTYSTRING);
    }

    public static String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            todo request permissions
            return "unknown";
        }
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2g";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3g";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4g";
            case TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_IWLAN:
            case TelephonyManager.NETWORK_TYPE_NR:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return "unknown";
        }
    }

    public static void setCursorDrawableColor(Context context, EditText editText, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Drawable drawable = editText.getTextCursorDrawable();
            drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_IN));
            editText.setTextCursorDrawable(drawable);
        }
    }

    public static int nOfBytes(File file) {
        int count = 0;
        try {
            FileInputStream in = new FileInputStream(file);

            while (in.read() != -1) count++;

            return count;
        } catch (IOException e) {
            Tuils.log(e);
            return count;
        }
    }

    public static void sendXMLParseError(Context context, String PATH, SAXParseException e) {
        Tuils.sendOutput(
                Color.RED,
                context, context.getString(R.string.output_xmlproblem1) + Tuils.SPACE + PATH + context.getString(R.string.output_xmlproblem2) + Tuils.NEWLINE + context.getString(R.string.output_errorlabel) +
                        "File: " + e.getSystemId() + Tuils.NEWLINE +
                        "Message" + e.getMessage() + Tuils.NEWLINE +
                        "Line" + e.getLineNumber() + Tuils.NEWLINE +
                        "Column" + e.getColumnNumber());
    }

    public static void sendXMLParseError(Context context, String PATH) {
        Tuils.sendOutput(Color.RED, context, context.getString(R.string.output_xmlproblem1) + Tuils.SPACE + PATH + context.getString(R.string.output_xmlproblem2));
    }

}
