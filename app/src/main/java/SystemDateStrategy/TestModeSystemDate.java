package SystemDateStrategy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.charl.walkthisway.R;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class TestModeSystemDate implements SystemDate {

    SystemDatePreferenceManager sdpm = new SystemDatePreferenceManager();

    @Override
    public String setSystemDate(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String defaultdate = sdf.format(new Date(System.currentTimeMillis()));
        String date = pref.getString("test_date", defaultdate);
        return date;
    }
}
