package com.bencarlisle15.terminalhomelauncher.commands.tuixt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.Command;
import com.bencarlisle15.terminalhomelauncher.commands.CommandGroup;
import com.bencarlisle15.terminalhomelauncher.commands.CommandTuils;
import com.bencarlisle15.terminalhomelauncher.managers.xml.XMLPrefsManager;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Theme;
import com.bencarlisle15.terminalhomelauncher.managers.xml.options.Ui;
import com.bencarlisle15.terminalhomelauncher.tuils.StoppableThread;
import com.bencarlisle15.terminalhomelauncher.tuils.Tuils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by francescoandreuzzi on 19/01/2017.
 */

public class TuixtActivity extends Activity {

    private final static String FIRSTACCESS_KEY = "firstAccess";

    public static final int BACK_PRESSED = 2;

    private long lastEnter;

    public static final String PATH = "path";

    public static final String ERROR_KEY = "error";

    private EditText inputView;
    private EditText fileView;
    private TextView outputView;

    private TuixtPack pack;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout rootView = new LinearLayout(this);

        final Intent intent = getIntent();

        String path = intent.getStringExtra(PATH);
        if (path == null) {
            Uri uri = intent.getData();
            File file = new File(uri.getPath());
            path = file.getAbsolutePath();
        }

        final File file = new File(path);

        CommandGroup group = new CommandGroup(this, "com.bencarlisle15.terminalhomelauncher.commands.tuixt.raw");

        try {
            XMLPrefsManager.loadCommons(this);
        } catch (Exception e) {
            finish();
        }

        if (!XMLPrefsManager.getBoolean(Ui.ignore_bar_color)) {
            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(XMLPrefsManager.getColor(Theme.statusbar_color));
            window.setNavigationBarColor(XMLPrefsManager.getColor(Theme.navigationbar_color));
        }

        if (!XMLPrefsManager.getBoolean(Ui.system_wallpaper)) {
            rootView.setBackgroundColor(XMLPrefsManager.getColor(Theme.bg_color));
        } else {
            setTheme(R.style.Custom_SystemWP);
            rootView.setBackgroundColor(XMLPrefsManager.getColor(Theme.overlay_color));
        }

        final boolean inputBottom = XMLPrefsManager.getBoolean(Ui.input_bottom);
        int layoutId = inputBottom ? R.layout.tuixt_view_input_down : R.layout.tuixt_view_input_up;

        LayoutInflater inflater = getLayoutInflater();
        View inputOutputView = inflater.inflate(layoutId, null);
        rootView.addView(inputOutputView);

        fileView = inputOutputView.findViewById(R.id.file_view);
        inputView = inputOutputView.findViewById(R.id.input_view);
        outputView = inputOutputView.findViewById(R.id.output_view);

        TextView prefixView = inputOutputView.findViewById(R.id.prefix_view);

        ImageButton submitView = inputOutputView.findViewById(R.id.submit_tv);
        boolean showSubmit = XMLPrefsManager.getBoolean(Ui.show_enter_button);
        if (!showSubmit) {
            submitView.setVisibility(View.GONE);
            submitView = null;
        }

        String prefix = XMLPrefsManager.get(Ui.input_prefix);

        int ioSize = XMLPrefsManager.getInt(Ui.input_output_size);
        int outputColor = XMLPrefsManager.getColor(Theme.output_color);
        int inputColor = XMLPrefsManager.getColor(Theme.input_color);

        prefixView.setTypeface(Tuils.getTypeface(this));
        prefixView.setTextColor(inputColor);
        prefixView.setTextSize(ioSize);
        prefixView.setText(prefix.endsWith(Tuils.SPACE) ? prefix : prefix + Tuils.SPACE);

        if (submitView != null) {
            submitView.setColorFilter(XMLPrefsManager.getColor(Theme.enter_color));
            submitView.setOnClickListener(v -> onNewInput());
        }

        fileView.setTypeface(Tuils.getTypeface(this));
        fileView.setTextSize(ioSize);
        fileView.setTextColor(outputColor);
        fileView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("FILE", "ON TOUCH");
                outputView.setVisibility(View.GONE);
                outputView.setText(Tuils.EMPTYSTRING);
            }
            return false;
        });

        outputView.setTypeface(Tuils.getTypeface(this));
        outputView.setTextSize(ioSize);
        outputView.setTextColor(outputColor);
        outputView.setMovementMethod(new ScrollingMovementMethod());
        outputView.setVisibility(View.GONE);

        inputView.setTypeface(Tuils.getTypeface(this));
        inputView.setTextSize(ioSize);
        inputView.setTextColor(inputColor);
        inputView.setHint(Tuils.getHint(path));

        inputView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        inputView.setOnEditorActionListener((v, actionId, event) -> {
//                physical enter
            if (actionId == KeyEvent.ACTION_DOWN) {
                if (lastEnter == 0) {
                    lastEnter = System.currentTimeMillis();
                } else {
                    long difference = System.currentTimeMillis() - lastEnter;
                    lastEnter = System.currentTimeMillis();
                    if (difference < 350) {
                        return true;
                    }
                }
            }

            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.ACTION_DOWN) {
                onNewInput();
            }
            return true;
        });

        setContentView(rootView);

//
//
//        end setup part, now start

        pack = new TuixtPack(group, file, this, fileView);

        String fileText = getString(R.string.tuixt_reading) + Tuils.SPACE + path;
        fileView.setText(fileText);
        new StoppableThread() {

            @Override
            public void run() {
                super.run();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));

                    final StringBuilder builder = new StringBuilder();
                    String line, lastLine = null;
                    while ((line = reader.readLine()) != null) {
                        if (lastLine != null) {
                            builder.append(Tuils.NEWLINE);
                        }
                        builder.append(line);
                        lastLine = line;
                    }

                    runOnUiThread(() -> {
                        try {
                            fileView.setText(builder.toString());
                        } catch (OutOfMemoryError e) {
                            fileView.setText(Tuils.EMPTYSTRING);
                            Toast.makeText(TuixtActivity.this, R.string.tuixt_error, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("bencarlisle15", "", e);
                    intent.putExtra(ERROR_KEY, e.toString());
                    setResult(1, intent);
                    finish();
                } catch (Error er) {
                    runOnUiThread(() -> {
                        System.gc();

                        fileView.setText(Tuils.EMPTYSTRING);
                        Toast.makeText(TuixtActivity.this, R.string.tuixt_error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        }.start();

        SharedPreferences preferences = getPreferences(0);
        boolean firstAccess = preferences.getBoolean(FIRSTACCESS_KEY, true);
        if (firstAccess) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(FIRSTACCESS_KEY, false);
            editor.apply();

            String helpString = "help";
            inputView.setText(helpString);
            inputView.setSelection(inputView.getText().length());
        }
    }

    @Override
    public void onBackPressed() {
        setResult(BACK_PRESSED);
        finish();
    }
    
    private void onNewInput() {
        try {
            String input = inputView.getText().toString();
            inputView.setText(Tuils.EMPTYSTRING);

            input = input.trim();
            if (input.length() == 0) {
                return;
            }

            outputView.setVisibility(View.VISIBLE);

            Command command = CommandTuils.parse(input, pack);
            if (command == null) {
                outputView.setText(R.string.output_commandnotfound);
                return;
            }

            String output = command.exec(pack);
            if (output != null) {
                outputView.setText(output);
            }
        } catch (Exception e) {
            outputView.setText(e.toString());
        }
    }
}
