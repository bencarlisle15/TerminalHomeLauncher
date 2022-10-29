package com.bencarlisle15.terminalhomelauncher.tuils;

import androidx.core.content.FileProvider;

import com.bencarlisle15.terminalhomelauncher.BuildConfig;

public class GenericFileProvider extends FileProvider {
    public static final String PROVIDER_NAME = BuildConfig.APPLICATION_ID + ".FILE_PROVIDER";
}
