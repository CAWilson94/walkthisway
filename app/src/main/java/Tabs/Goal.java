package Tabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charl.walkthisway.AlarmHandler;
import com.example.charl.walkthisway.Calculations;
import com.example.charl.walkthisway.EndOfDay;
import com.example.charl.walkthisway.R;

import SystemDateStrategy.SystemDateManager;
import SystemDateStrategy.SystemDatePreferenceManager;

import com.natasa.progressviews.CircleSegmentBar;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Dialogs.AddActivity;
import Dialogs.CreateNewGoal;
import Dialogs.EditGoal;
import Models.DbManager;

import static android.R.attr.filter;
import static android.content.Context.ALARM_SERVICE;
import static com.example.charl.walkthisway.R.id.circle_progress;
import static com.example.charl.walkthisway.R.id.yermaw;
import static com.example.charl.walkthisway.UIUtils.setListViewHeightBasedOnItems;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Goal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Goal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Goal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AlarmHandler ah = new AlarmHandler();

    Calculations calculations = new Calculations();

    private SharedPreferences.OnSharedPreferenceChangeListener prefListner;


    SystemDatePreferenceManager us = new SystemDatePreferenceManager();
    CardView cardView;
    CardView cardListView;
    com.natasa.progressviews.CircleSegmentBar circleProgressBar;

    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    Cursor cursor;
    SimpleCursorAdapter myCursorAdapter;

    View v;
    ListView myList;
    Bundle args = new Bundle();
    private OnFragmentInteractionListener mListener;

    public Goal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Goal.
     */
    // TODO: Rename and change types and number of parameters
    public static Goal newInstance(String param1, String param2) {
        Goal fragment = new Goal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        EndOfDay endOfDay = new EndOfDay();
        IntentFilter filter = new IntentFilter("CONTENTS_NOTIFICATION");
        getContext().registerReceiver(endOfDay, filter);


        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        checkActiveGoalCard();
        populateListView();
        circleProgressBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        prefListner = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                // Somehow check if date actually changed..
                db.carriageToPumpkin();
                checkActiveGoalCard();
                populateListView();
            }
        };

        pref.registerOnSharedPreferenceChangeListener(prefListner); // Otherwise garbage collection.. :(

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        db = new DbManager(getActivity(), null, null, 2);

        //GoalHandler goalHandler = new GoalHandler();
        //goalHandler.createRandomGoals(db);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ah.alarmRepeat(getContext()); // will this schedule it on every open ??
        // Lets check for test mode
        v = inflater.inflate(R.layout.fragment_stats, container, false);
        circleProgressBar = (com.natasa.progressviews.CircleSegmentBar) v.findViewById(R.id.circle_progress);
        circleProgressBar();
        cardView = (CardView) v.findViewById(R.id.main_progress_card);
        cardListView = (CardView) v.findViewById(R.id.card_view_goals_list);
        // updating card view
        checkActiveGoalCard();
        myList = (ListView) v.findViewById(R.id.list_goals); // get list view into main activity
        populateListView(); // populate list!

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_activity_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                CreateNewGoal newGoal = new CreateNewGoal();
                newGoal.setTargetFragment(Goal.this, 0);
                newGoal.show(fm, "Add New Goal");
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            populateListView();
            checkActiveGoalCard();
            circleProgressBar();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getProgress() {
        float progress = 0;
        int currentSteps = db.displayActiveSteps();
        int goalSteps = db.displayGoalSteps();
        progress = Math.round((currentSteps * 100.0f) / goalSteps);
        //db.close();
        return (int) progress;
    }

    private void circleProgressBar() {
        this.circleProgressBar.setProgress((int) db.displayActiveSteps()); // Sign of weakness sue me!!
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void checkActiveGoalCard() {


        // check for items in db..
        if (db.checkActiveGoal()) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = getFragmentManager();
                    AddActivity dialogFragment = new AddActivity();
                    dialogFragment.setTargetFragment(Goal.this, 0);
                    dialogFragment.show(fm, "Add Activity");
                }
            });

        }

        String test = "testing";
        TextView text = (TextView) cardView.findViewById(R.id.textView3);
        TextView stepsActive = (TextView) cardView.findViewById(yermaw);
        CircleSegmentBar csb = (CircleSegmentBar) cardView.findViewById(circle_progress);

        SystemDateManager date = new SystemDateManager();
        String systemorUserDate = date.systemDateDecider(this.getActivity());

        if (!db.checkForActiveGoal(this.getActivity(), systemorUserDate)) {
            String pleaseEnterGoal = "There is no active goal, please pick one from the list or click here to create a goal";
            stepsActive.setText(String.valueOf("0" + " / " + "0"));
            this.circleProgressBar.setProgress(0);
            text.setText(pleaseEnterGoal);
        } else {
            text.setText(db.displayActiveName());

            Calculations calculations = new Calculations();
            int currentSteps = db.displayActiveSteps();
            int goalSteps = db.displayGoalSteps();
            double current = calculations.doConversion(db.displayUnitsFromActiveStatus(), (double) currentSteps);
            double goal = calculations.doConversion(db.displayUnitsFromActiveStatus(), (double) goalSteps);


            stepsActive.setText(String.valueOf(current) + " / " + goalSteps + "\n        " + db.displayUnitsFromActiveStatus());
        }
        //csb.setVisibility(View.INVISIBLE);
        //db.close();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public View populateListView() {
        cursor = db.getAllRows(this.getActivity());

        String[] fromFieldNames = new String[]{db.COLUMN_DATE_GOALS, db.COLUMN_GOAL_NAME,
                db.COLUMN_CURRENT_STEPS, db.COLUMN_STEP_GOALS}; // Placeholder

        int[] toViewIDs = new int[]{R.id.date_goal_added, R.id.goal_name, R.id.current_progress_current, R.id.current_progress_total,}; // Placeholder
        // Set up the adapter
        myCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.custom_row, cursor, fromFieldNames, toViewIDs, 0);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) myCursorAdapter.getItem(position);
                String myColumnValue = cursor.getString(cursor.getColumnIndex(db.COLUMN_GOAL_ID));

                FragmentManager fm = getFragmentManager();
                EditGoal dialogFragment = new EditGoal();

                Bundle args = new Bundle();
                args.putString("IDYAS", myColumnValue);
                dialogFragment.setArguments(args);

                dialogFragment.setTargetFragment(Goal.this, 0);
                dialogFragment.show(fm, "Add Activity");

            }
        });

        myCursorAdapter.changeCursor(db.getAllRows(getContext()));
        myList.setAdapter(myCursorAdapter);
        myList.setFocusable(false);

        setListViewHeightBasedOnItems(myList, cardListView);
        //db.close();
        return v;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        if (!us.sharedPrefTestMode(getContext())) {
            cal_test.setVisible(false);
            test_mode.setVisible(false);
        }
        item.setVisible(false);
    }


}
