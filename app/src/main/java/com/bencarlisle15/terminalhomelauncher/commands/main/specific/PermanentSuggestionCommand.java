package com.bencarlisle15.terminalhomelauncher.commands.main.specific;

import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;

/**
 * Created by francescoandreuzzi on 29/01/2017.
 */

public abstract class PermanentSuggestionCommand implements CommandAbstraction {

    public abstract String[] permanentSuggestions();
}
