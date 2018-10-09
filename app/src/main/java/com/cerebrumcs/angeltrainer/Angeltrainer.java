package com.cerebrumcs.angeltrainer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cerebrumcs.angeltrainer.database.DatabaseUtils;

import java.io.File;

public class Angeltrainer extends Application {

    private static String APP_ID = "com.cerebrumcs.angeltrainer";

    private static String FIRST_RUN = "firstrun_version";
    private static int VERSION = 10;
    private static Angeltrainer instance;

    public static Angeltrainer getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        SharedPreferences preferences = getSharedPreferences(APP_ID, MODE_PRIVATE);

        if(preferences.getInt(FIRST_RUN, VERSION) == VERSION){
            DatabaseUtils.deleteDb();
            DatabaseUtils.fillDatabase(getAssets());
            preferences.edit().clear();
            preferences.edit().putInt(FIRST_RUN, VERSION+1).commit();
        }

        super.onCreate();
    }

}
