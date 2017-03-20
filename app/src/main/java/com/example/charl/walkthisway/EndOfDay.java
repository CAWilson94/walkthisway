package com.example.charl.walkthisway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import Models.DbManager;
import Tabs.Goal;

/**
 * Created by charl on 20/03/2017.
 */

public class EndOfDay extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DbManager db = new DbManager(context, null, null, DbManager.DATABASE_VERSION);
        Toast.makeText(context, "YASSSSSSSS", Toast.LENGTH_LONG).show();
        Log.d("TAG", "YASSSSSSSSSSS------------------------");
        Log.d("DEBUG", "HELP PLS" + System.currentTimeMillis());
        db.dayPassed();
        db.carriageToPumpkin();



    }
}
