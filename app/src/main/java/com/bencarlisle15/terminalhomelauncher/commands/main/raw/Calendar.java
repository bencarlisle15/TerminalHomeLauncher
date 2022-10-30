package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bencarlisle15.terminalhomelauncher.LauncherActivity;
import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;
import com.bencarlisle15.terminalhomelauncher.commands.main.MainPack;
import com.bencarlisle15.terminalhomelauncher.commands.main.specific.ParamCommand;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Calendar extends ParamCommand {

    private static String getTimeString(java.util.Calendar time) {
        SimpleDateFormat formatter = new SimpleDateFormat("M/d h:mm a", Locale.getDefault());
        return formatter.format(time.getTime());
    }

    private static long getCalendarId(Context context) {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = {CalendarContract.Events._ID};
        String selection = CalendarContract.Calendars.VISIBLE + " = 1 AND "  + CalendarContract.Calendars.IS_PRIMARY + " = 1";
        String ordering = CalendarContract.Calendars._ID + " ASC";

        Cursor calendarIdCursor = context.getContentResolver().query(uri, projection, selection, null, ordering);

        // from android documentation as default
        long calendarId = 3;
        if (calendarIdCursor.moveToNext()) {
            calendarId = calendarIdCursor.getLong(0);
        }
        calendarIdCursor.close();
        return calendarId;
    }

    private static java.util.Calendar parseDateTimeString(String dateString, String timeString) throws ParseException {
        SimpleDateFormat format;
        String dateTimeString;
        if (dateString == null) {
            format = new SimpleDateFormat("h:mma", Locale.getDefault());
            dateTimeString = timeString;
        } else  {
            format = new SimpleDateFormat("M/d h:mma", Locale.getDefault());
            dateTimeString = dateString + " " + timeString;
        }
        format.setTimeZone(TimeZone.getDefault());
        Date date = format.parse(dateTimeString);
        if (date == null) {
            throw new ParseException("Could not parse date time string: " + timeString, 0);
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);

//        date needs to be set to today
        java.util.Calendar dateCorrectedCalendar = java.util.Calendar.getInstance();
        dateCorrectedCalendar.set(java.util.Calendar.HOUR_OF_DAY, calendar.get(java.util.Calendar.HOUR_OF_DAY));
        dateCorrectedCalendar.set(java.util.Calendar.MINUTE, calendar.get(java.util.Calendar.MINUTE));

        if (dateString != null) {
            dateCorrectedCalendar.set(java.util.Calendar.DAY_OF_YEAR, calendar.get(java.util.Calendar.DAY_OF_YEAR));
        }

        return dateCorrectedCalendar;
    }

    private static String createTitle(List<String> params, int startIndex) {
        StringBuilder title = new StringBuilder(params.get(startIndex++));
        for (int i = startIndex; i < params.size(); i++) {
            title.append(" ");
            title.append(params.get(i));
        }
        return title.toString();
    }

    private static String addEvent(Context context, String dateString, String timeString, String title) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
            return context.getString(R.string.output_waitingpermission);
        }

        long calendarId = getCalendarId(context);
        java.util.Calendar startTime;
        try {
            startTime = parseDateTimeString(dateString, timeString);
        } catch (ParseException e) {
            if (dateString == null) {
                return "Could not parse time string: " + timeString;
            }
            return "Could not parse date time string: " + dateString + " " + timeString;
        }

//                should be made ahead of time
        if (dateString == null && startTime.getTimeInMillis() < java.util.Calendar.getInstance().getTimeInMillis()) {
            startTime.add(java.util.Calendar.HOUR_OF_DAY, 24);
        }

        long startMillis = startTime.getTimeInMillis();

        java.util.Calendar endTime = java.util.Calendar.getInstance();
        endTime.setTimeInMillis(startMillis);
        endTime.add(java.util.Calendar.HOUR, 1);
        long endMillis = endTime.getTimeInMillis();

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);

        return "Created event: " + getTimeString(startTime) + " => " + title;
    }

    private static String deleteEvents(Context context, String selection, String[] selectionArgs) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
            return context.getString(R.string.output_waitingpermission);
        }

        ContentResolver contentResolver = context.getContentResolver();
        int numDeleted = contentResolver.delete(CalendarContract.Events.CONTENT_URI, selection, selectionArgs);

        return "Deleted: " + numDeleted + " events";
    }

    private enum Param implements com.bencarlisle15.terminalhomelauncher.commands.main.Param {
        list {
            @Override
            public String exec(ExecutePack pack) {
                if (ContextCompat.checkSelfPermission(pack.context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) pack.context, new String[]{Manifest.permission.READ_CALENDAR}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
                    return pack.context.getString(R.string.output_waitingpermission);
                }

                ContentResolver contentResolver = pack.context.getContentResolver();
                Uri uri = CalendarContract.Events.CONTENT_URI;

                java.util.Calendar time = java.util.Calendar.getInstance();
                long currentTimeMillis = time.getTimeInMillis();
                time.add(java.util.Calendar.HOUR_OF_DAY, 24);
                long endOfDayMillis = time.getTimeInMillis();

                String selection = "(" + CalendarContract.Events.DTSTART + " >= ?) AND (" + CalendarContract.Events.DTSTART + " <= ?) AND  (" + CalendarContract.Events.DELETED + " = 0)";
                String[] selectionArgs = new String[] {String.valueOf(currentTimeMillis), String.valueOf(endOfDayMillis)};
                String[] eventProjection = new String[]{
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.TITLE,
                };
                String ordering = CalendarContract.Events.DTSTART + " ASC";
                Cursor cursor = contentResolver.query(uri, eventProjection, selection, selectionArgs, ordering);

                StringBuilder result = new StringBuilder("Events:");
                int i = 1;
                while (cursor.moveToNext()) {
                    java.util.Calendar eventTime = java.util.Calendar.getInstance();
                    eventTime.setTimeInMillis(cursor.getLong(0));


                    result.append("\n");
                    result.append(i++);
                    result.append(". ");
                    result.append(getTimeString(eventTime));
                    result.append(" => ");
                    result.append(cursor.getString(1));
                }
                cursor.close();
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
                return new int[]{CommandAbstraction.TEXTLIST};
            }

            @Override
            public String exec(ExecutePack pack) {
                List<String> params = pack.getList();

                if (params.size() < 2) {
                    return "Invalid params, expecting: calendar -add 12:00pm event title";
                }

                String timeString = params.get(0);
                String title = createTitle(params, 1);
                return addEvent(pack.context, null, timeString, title);
            }
        },
        add_with_date {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.TEXTLIST};
            }

            @Override
            public String exec(ExecutePack pack) {
                List<String> params = pack.getList();

                if (params.size() < 3) {
                    return "Invalid params, expecting: calendar -add_with_date 1/1 12:00pm event title";
                }

                String dateString = params.get(0);
                String timeString = params.get(1);
                String title = createTitle(params, 2);
                return addEvent(pack.context, dateString, timeString, title);
            }
        },
        delete {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.TEXTLIST};
            }

            @Override
            public String exec(ExecutePack pack) {
                List<String> params = pack.getList();

                if (params.size() < 1 || params.size() > 2) {
                    return "Invalid params, expecting: calendar -delete 1/1 12:00pm or calendar -delete_with_date 12:00pm";
                }

                String dateString = null;
                String timeString;
                if (params.size() == 1) {
                    timeString = params.get(0);
                } else {
                    dateString = params.get(0);
                    timeString = params.get(1);
                }

                java.util.Calendar dateTime;
                try {
                    dateTime = parseDateTimeString(dateString, timeString);
                } catch (ParseException e) {
                    return "Could not parse date time string: " + dateString + " " + timeString;
                }

                //                should be made ahead of time
                if (dateString == null && dateTime.getTimeInMillis() < java.util.Calendar.getInstance().getTimeInMillis()) {
                    dateTime.add(java.util.Calendar.HOUR_OF_DAY, 24);
                }


                String selection = "Abs(" + CalendarContract.Events.DTSTART + " - ?) < 60000";
                String[] selectionArgs = new String[] {String.valueOf(dateTime.getTimeInMillis())};

                return deleteEvents(pack.context, selection, selectionArgs);
            }
        },
        delete_by_name {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.TEXTLIST};
            }

            @Override
            public String exec(ExecutePack pack) {
                List<String> params = pack.getList();

                if (params.size() < 1) {
                    return "Invalid params";
                }

                String title = createTitle(params, 0);

                String selection = "(" + CalendarContract.Events.TITLE + " = ?)";
                String[] selectionArgs = new String[] {title};

                return deleteEvents(pack.context, selection, selectionArgs);
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
