package SystemDateStrategy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.charl.walkthisway.R;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class TestModeSystemDate implements SystemDate {

    SystemDatePreferenceManager sdpm = new SystemDatePreferenceManager();

    @Override
    public String setSystemDate(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String date = pref.getString("test_date", "help me...");
        return null;
    }
}
