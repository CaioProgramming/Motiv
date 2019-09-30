package com.creat.motiv;

import android.app.Application;
import android.content.Intent;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable e) {
                        handleUncaughtException(thread, e);
                    }
                });
    }

    private void handleUncaughtException(Thread thread, Throwable e) {
        Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //Log.println(Log.ERROR, "Erro", e.getMessage());

    }

}
