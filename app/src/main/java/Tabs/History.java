package Tabs;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charl.walkthisway.Calculations;
import com.example.charl.walkthisway.MainActivity;
import com.example.charl.walkthisway.Populate;
import com.example.charl.walkthisway.R;

import Dialogs.ClearHistoryWarning;
import Dialogs.CreateNewGoal;
import Models.DbManager;
import SystemDateStrategy.SystemDatePreferenceManager;

import static android.R.attr.name;
import static com.example.charl.walkthisway.R.id.test;
import static com.example.charl.walkthisway.R.id.view;
import static com.example.charl.walkthisway.UIUtils.setListViewHeightBasedOnItems;


public class History extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SystemDatePreferenceManager us = new SystemDatePreferenceManager();
    Calculations calulations = new Calculations();

    private Cursor cursor;
    ListView myList;
    CardView cardListView;
    String time[] = {"Day view", "Week view", "Month View", "Custom View"};
    String units[] = {calulations.STEPS, calulations.KM, calulations.METRES, calulations.MILES, calulations.YARDS};
    String complete[] = {"Complete", "All"};
    private View v;
    private SimpleCursorAdapter myCursorAdapter;
    String spinnerUnits;

    private Populate pop = new Populate();

    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        db = new DbManager(getActivity(), null, null, 2);
    }

    @Override
    public void onPause() {
        //populateListView();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d("TAG", "on RESUME HAS BEEN CALLED -------------------------------");
        //populateListView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_test, container, false);
        CardView cardView = (CardView) v.findViewById(R.id.card_history);
        Spinner spinnerCompleteView = (Spinner) v.findViewById(R.id.spinner_complete);
        Spinner spinnerTimeView = (Spinner) v.findViewById(R.id.spinner_time);
        final Spinner spinnerUnitsView = (Spinner) v.findViewById(R.id.spinner_units);
        pop.populateSpinner(spinnerTimeView, time, getContext());
        pop.populateSpinner(spinnerUnitsView, units, getContext());
        pop.populateSpinner(spinnerCompleteView, complete, getContext());

        myList = (ListView) v.findViewById(R.id.history_list); // get list view into main activity


        String completeOrAll = spinnerCompleteView.getSelectedItem().toString();

        spinnerUnitsView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerUnits = spinnerUnitsView.getItemAtPosition(position).toString();
                populateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public View populateListView() {


        DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);
        cursor = db.simpleHistory();
        String[] fromFieldNames = new String[]{db.COLUMN_GOAL_UNITS, db.COLUMN_DATE_GOALS, db.COLUMN_GOAL_NAME,
                db.COLUMN_CURRENT_STEPS, db.COLUMN_STEP_GOALS}; // Placeholder

        int[] toViewIDs = new int[]{R.id.unit_goals_list, R.id.date_goal_added, R.id.goal_name, R.id.current_progress_current, R.id.current_progress_total}; // Placeholder

        // Set up the adapter
        myCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.custom_row, cursor, fromFieldNames, toViewIDs, 0);


        myCursorAdapter.changeCursor(db.simpleHistory());

        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                                          @Override
                                          public boolean setViewValue(View v, Cursor cursor, int acolumnIndex) {


                                              if (acolumnIndex == 8) { // Will need to update not only the string output but, the numbers
                                                  Log.d("COLUMN THREE PLEASE: ", cursor.getString(acolumnIndex));
                                                  //String units= cursor.getString(acolumnIndex);
                                                  TextView textView = (TextView) v;
                                                  // Convert from those units to whatever was asked for.
                                                  textView.setText(spinnerUnits);
                                                  return true;
                                              }

                                              if (acolumnIndex == 5) { // Will need to update not only the string output but, the numbers
                                                  Log.d("COLUMN FOUR PLEASE: ", String.valueOf(acolumnIndex));
                                                  String stepGoal = cursor.getString(acolumnIndex);
                                                  TextView textView = (TextView) v;

                                                  double stepConvertGoal = calulations.doConversion(spinnerUnits, Double.parseDouble(stepGoal));


                                                  //Toast.makeText(getContext(), "hello: " + spinnerUnits, Toast.LENGTH_LONG).show();
                                                  // Convert from those units to whatever was asked for.
                                                  textView.setText(String.valueOf(stepConvertGoal));
                                                  return true;
                                              }

                                              if (acolumnIndex == 2) { // Will need to update not only the string output but, the numbers
                                                  String step = cursor.getString(acolumnIndex);
                                                  TextView textView = (TextView) v;
                                                  double stepConvert = calulations.doConversion(spinnerUnits, Double.parseDouble(step));
                                                  //Toast.makeText(getContext(), "hello: " + spinnerUnits, Toast.LENGTH_LONG).show();
                                                  // Convert from those units to whatever was asked for.
                                                  textView.setText(String.valueOf(stepConvert));
                                                  return true;
                                              }
                                              return false;
                                          }

                                      }


        );


        myList.setAdapter(myCursorAdapter);


        myList.setFocusable(false);
        setListViewHeightBasedOnItems(myList, cardListView);


        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_delete_history);
        MenuItem cal_test = menu.findItem(R.id.action_date_picker);
        MenuItem test_mode = menu.findItem(R.id.test_mode_menu);
        if (us.sharedPrefTestMode(getContext())) {
            cal_test.setVisible(true);
            test_mode.setVisible(true);
        }
        item.setVisible(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            DbManager dbd = new DbManager(this.getActivity().getApplicationContext(), null, null, DbManager.DATABASE_VERSION);
            dbd.clearHistory();
            populateListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_history) {

            FragmentManager fm = getFragmentManager();
            ClearHistoryWarning clear = new ClearHistoryWarning();
            clear.setTargetFragment(History.this, 0);
            clear.show(fm, "Add New Goal");
        }
        return super.onOptionsItemSelected(item);
    }
}
