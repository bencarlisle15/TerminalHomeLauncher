package com.bencarlisle15.terminalhomelauncher.tuils.interfaces;

import com.bencarlisle15.terminalhomelauncher.commands.main.specific.RedirectCommand;

/**
 * Created by francescoandreuzzi on 03/03/2017.
 */

public interface OnRedirectionListener {

    void onRedirectionRequest(RedirectCommand cmd);
    void onRedirectionEnd(RedirectCommand cmd);
}
