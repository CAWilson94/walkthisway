package com.example.charl.walkthisway;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Charlotte on 18/03/2017.
 */

public class UsefulShit {


    public Boolean sharedPrefTestMode(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean testMode = pref.getBoolean("test_mode", false);

        if (testMode)
            return true;
        return false;
    }
}
