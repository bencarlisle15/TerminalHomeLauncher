package ohi.andre.consolelauncher.tuils;

import androidx.annotation.NonNull;

/**
 * Created by francescoandreuzzi on 22/02/2018.
 */

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler _defaultEH;

    public CustomExceptionHandler(){
        _defaultEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull final Throwable ex) {
        Tuils.toFile(ex);
        _defaultEH.uncaughtException(thread, ex);
    }

}