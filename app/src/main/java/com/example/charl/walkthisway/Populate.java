package com.example.charl.walkthisway;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class Populate {

    public void populateSpinner(Spinner spinner, String[] ddInfo, Context context) {
        // Basically the same as using a listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, ddInfo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
