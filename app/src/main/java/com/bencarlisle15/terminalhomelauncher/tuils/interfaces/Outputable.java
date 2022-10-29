package com.bencarlisle15.terminalhomelauncher.tuils.interfaces;

/**
 * Created by bencarlisle15 on 25/07/15.
 */
public interface Outputable {
    void onOutput(CharSequence output, int category);
    void onOutput(int color, CharSequence output);
    void onOutput(CharSequence output);
    void dispose();
}
