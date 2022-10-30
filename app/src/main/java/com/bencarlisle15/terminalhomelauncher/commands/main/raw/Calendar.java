package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bencarlisle15.terminalhomelauncher.BuildConfig;
import com.bencarlisle15.terminalhomelauncher.LauncherActivity;
import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.commands.main.specific.ParamCommand;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

import java.util.List;

/**
 * Created by francescoandreuzzi on 22/08/2017.
 */

public class Calendar extends ParamCommand {

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    private enum Param implements com.bencarlisle15.terminalhomelauncher.commands.main.Param {
        list {
            @Override
            public String exec(ExecutePack pack) {

                if (ContextCompat.checkSelfPermission(pack.context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) pack.context, new String[]{Manifest.permission.READ_CALENDAR}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
                    return pack.context.getString(R.string.output_waitingpermission);
                }

                Cursor cur = null;
                ContentResolver cr = pack.context.getContentResolver();
                Uri uri = CalendarContract.Calendars.CONTENT_URI;
                String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                        + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
                String[] selectionArgs = new String[] {"hera@example.com", "com.example",
                        "hera@example.com"};
// Submit the query and get a Cursor object back.
                cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

                StringBuilder result = new StringBuilder("Result:\n");

                while (cur.moveToNext()) {
                    long calID = 0;
                    String displayName = null;
                    String accountName = null;
                    String ownerName = null;

                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                    accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                    ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                    result.append(calID);
                    result.append(", ");
                    result.append(displayName);
                    result.append(", ");
                    result.append(accountName);
                    result.append(", ");
                    result.append(ownerName);
                    result.append("\n");

                    // Do something with the values...
                }
                cur.close();


                return result.toString();
            }

            @Override
            public int[] args() {
                return new int[0];
            }
        },
        add {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.TEXTLIST, CommandAbstraction.TEXTLIST};
            }

            @Override
            public String exec(ExecutePack pack) {
                return "Ran set " + pack.getList();
            }
        };

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
            return pack.context.getString(R.string.help_calendar);
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
    public int priority() {
        return 2;
    }

    @Override
    public int helpRes() {
        return R.string.help_calendar;
    }

    @Override
    public String[] params() {
        return Param.labels();
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }
}
