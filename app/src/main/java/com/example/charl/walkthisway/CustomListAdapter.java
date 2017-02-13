package com.example.charl.walkthisway;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Charlotte on 11/02/2017.
 */

public class CustomListAdapter extends ArrayAdapter {
    public CustomListAdapter(Context context, String[] goals) {
        super(context, R.layout.custom_row, goals);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * For the strings that are passed in: put them in these positions in the layout
         */
        LayoutInflater inflater = LayoutInflater.from(getContext()); // Context just means background info
        View v = inflater.inflate(R.layout.custom_row, parent, false);
        /*
          * Now get a reference to everything: first the array (later db fields) then
          * The text viewss to be filled with name, current steps, total steps and competeOrNot
          * and also find a way to add in current date when new goal created
         */
        String goalName = String.valueOf(getItem(position)); //TODO: change to db
        TextView goalNameText = (TextView) v.findViewById(R.id.goal_name);
        TextView dateGoalAdded = (TextView) v.findViewById(R.id.date_goal_added);
        TextView currentProgressText = (TextView) v.findViewById(R.id.current_progress_added); //TODO: read docs online to see how to dynamically change
        CheckBox goalComplete = (CheckBox) v.findViewById(R.id.goal_complete);

        goalNameText.setText(goalName); // Can change this to DB read hurrah!
        return v;
    }


}
