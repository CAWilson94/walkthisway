package SystemDateStrategy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Charlotte on 18/03/2017.
 */

public class SystemDatePreferenceManager {

    SharedPreferences pref;


    public Boolean sharedPrefTestMode(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean testMode = pref.getBoolean("test_mode", false);
        if (testMode)
            return true;
        return false;
    }

    public void testModeDate(Date testDate, Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(testDate);
        editor.putString("test_date", date);
    }

    public void mainModeDate(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(System.currentTimeMillis()));
        editor.putString("main_date", date);
    }
}
