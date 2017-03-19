package SystemDateStrategy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Charlotte on 19/03/2017.
 */
public class MainModeSystemDate implements SystemDate {

    SystemDatePreferenceManager sdpm = new SystemDatePreferenceManager();

    @Override
    public String setSystemDate(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String date = pref.getString("main_date", "help me...");
        return null;
    }
}
