package com.example.charl.walkthisway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by charl on 20/03/2017.
 */

public class EndOfDay extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "YASSSSSSSS", Toast.LENGTH_LONG).show();
        Log.d("TAG", "YASSSSSSSSSSS------------------------");
    }
}
