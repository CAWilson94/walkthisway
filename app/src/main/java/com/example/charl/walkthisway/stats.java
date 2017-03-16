package com.example.charl.walkthisway;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.natasa.progressviews.CircleSegmentBar;

import Models.DbManager;

import static android.R.attr.data;
import static com.example.charl.walkthisway.R.id.circle_progress;
import static com.example.charl.walkthisway.R.id.view;
import static com.example.charl.walkthisway.R.id.yermaw;
import static com.example.charl.walkthisway.UIUtils.setListViewHeightBasedOnItems;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link stats.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link stats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stats extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View cardView;
    com.natasa.progressviews.CircleSegmentBar circleProgressBar;

    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    Cursor cursor;
    SimpleCursorAdapter myCursorAdapter;

    View v;
    ListView myList;
    Bundle args = new Bundle();
    private OnFragmentInteractionListener mListener;

    public stats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment stats.
     */
    // TODO: Rename and change types and number of parameters
    public static stats newInstance(String param1, String param2) {
        stats fragment = new stats();
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

        //((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_menu_camera);
        db = new DbManager(getActivity(), null, null, 2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the custom_row before trying to find the view

        v = inflater.inflate(R.layout.fragment_stats, container, false);
        circleProgressBar = (com.natasa.progressviews.CircleSegmentBar) v.findViewById(R.id.circle_progress);
        circleProgressBar(circleProgressBar);
        cardView = (CardView) v.findViewById(R.id.main_progress_card);
        checkActiveGoalCard(cardView);
        // List shit
        myList = (ListView) v.findViewById(R.id.list_goals); // get list view into main activity


        populateListView(); // populate list!

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                addActivity dialogFragment = new addActivity();
                dialogFragment.setTargetFragment(stats.this, 0);
                dialogFragment.show(fm, "Add Activity");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_activity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                CreateNewGoal newGoal = new CreateNewGoal();
                newGoal.setTargetFragment(stats.this, 0);
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
            circleProgressBar(circleProgressBar);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getProgress() {
        float progress = 0;
        int currentSteps = db.displayActiveSteps();
        int goalSteps = db.displayGoalSteps();
        progress = Math.round((currentSteps * 100.0f) / goalSteps);
        db.close();
        return (int) progress;
    }

    private void circleProgressBar(CircleSegmentBar circleProgressBar) {
        this.circleProgressBar.setProgress((int) getProgress()); // Sign of weakness sue me!!
    }

    private void checkActiveGoalCard() {
        String test = "testing";
        TextView text = (TextView) cardView.findViewById(R.id.textView3);
        TextView yermaw = (TextView) cardView.findViewById(R.id.yermaw);
        CircleSegmentBar csb = (CircleSegmentBar) cardView.findViewById(circle_progress);

        if (db.checkForActiveGoal() == false) {
            String pleaseEnterGoal = "There is no active goal, please pick one from the list or click here to create a goal";
            text.setText("pleaseEnterGoal");
            yermaw.setText(String.valueOf(db.displayActiveSteps()) + " / " + db.displayGoalSteps());
        } else {
            text.setText(db.getActiveGoalName());
            yermaw.setText(String.valueOf(db.displayActiveSteps()) + " / " + db.displayGoalSteps());
        }

        db.close();
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

    /**
     * Check for active goal in the goal card:
     * if there is one, display everything related to it.
     *
     * @param cardView
     */
    public void checkActiveGoalCard(View cardView) {

        String test = "testing";
        TextView text = (TextView) cardView.findViewById(R.id.textView3);
        TextView stepsActive = (TextView) cardView.findViewById(yermaw);
        CircleSegmentBar csb = (CircleSegmentBar) cardView.findViewById(circle_progress);

        if (!db.checkForActiveGoal()) {
            String pleaseEnterGoal = "There is no active goal, please pick one from the list or click here to create a goal";
            stepsActive.setText(String.valueOf(db.displayActiveSteps()) + " / " + db.displayGoalSteps());
            text.setText(pleaseEnterGoal);
        } else {
            text.setText(db.displayActiveName());
            stepsActive.setText(String.valueOf(db.displayActiveSteps()) + " / " + db.displayGoalSteps());
        }

        //csb.setVisibility(View.INVISIBLE);
        db.close();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public View populateListView() {
        cursor = db.getAllRows();

        String[] fromFieldNames = new String[]{DbManager.COLUMN_DATE_GOALS, DbManager.COLUMN_GOAL_NAME,
                DbManager.COLUMN_CURRENT_STEPS, DbManager.COLUMN_STEP_GOALS, DbManager.COLUMN_GOAL_COMPLETE}; // Placeholder

        int[] toViewIDs = new int[]{R.id.date_goal_added, R.id.goal_name, R.id.current_progress_current, R.id.current_progress_total, R.id.goal_complete_check}; // Placeholder
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

                dialogFragment.setTargetFragment(stats.this, 0);
                dialogFragment.show(fm, "Add Activity");

            }
        });

        myCursorAdapter.changeCursor(db.getAllRows());
        myList.setAdapter(myCursorAdapter);
        myList.setFocusable(false);
        setListViewHeightBasedOnItems(myList);
        db.close();
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


}
