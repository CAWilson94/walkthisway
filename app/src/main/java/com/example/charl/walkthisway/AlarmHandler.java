package com.example.charl.walkthisway;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static java.security.AccessController.getContext;

/**
 * Created by charl on 20/03/2017.
 */

public class AlarmHandler {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void alarmRepeat(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmInt = new Intent(context, EndOfDay.class);
        alarmInt.putExtra("key", "Alert");
        alarmIntent = PendingIntent.getBroadcast(context, 0, alarmInt, 0);

        Calendar calendar = Calendar.getInstance();
        // Calendar.set(int year, int month, int day, int hourOfDay, int minute, int second)
        calendar.set(2017, Calendar.MARCH, 20, 00, 0, 3);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.d("DEBUG", "HELP PLS" + System.currentTimeMillis());
    }
}
