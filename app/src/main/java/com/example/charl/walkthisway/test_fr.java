package com.example.charl.walkthisway;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import Models.DbManager;

import static com.example.charl.walkthisway.UIUtils.setListViewHeightBasedOnItems;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link test_fr.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link test_fr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class test_fr extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Cursor cursor;
    String time[] = {"Day view", "Week view", "Month View", "Custom View"};
    String units[] = {"Steps", "Km", "Miles", "Monroes.."};
    String complete[] = {"Complete", "All"};
    private View v;
    private SimpleCursorAdapter myCursorAdapter;
    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    public test_fr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment test_fr.
     */
    // TODO: Rename and change types and number of parameters
    public static test_fr newInstance(String param1, String param2) {
        test_fr fragment = new test_fr();
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

        db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_test, container, false);
        final CardView cardView = (CardView) v.findViewById(R.id.card_history);
        Spinner spinnerCompleteView = (Spinner) v.findViewById(R.id.spinner_complete);
        Spinner spinnerTimeView = (Spinner) v.findViewById(R.id.spinner_time);
        Spinner spinnerUnitsView = (Spinner) v.findViewById(R.id.spinner_units  );
        populateSpinner(spinnerTimeView, time);
        populateSpinner(spinnerUnitsView, units);
        populateSpinner(spinnerCompleteView, complete);
        populateListView();
        return v;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public View populateListView() {
        ListView myList;
        cursor = db.getAllRows();
        String[] fromFieldNames = new String[]{DbManager.COLUMN_DATE_GOALS,
                DbManager.COLUMN_GOAL_NAME, DbManager.COLUMN_CURRENT_STEPS, DbManager.COLUMN_STEP_GOALS, DbManager.COLUMN_GOAL_COMPLETE}; // Placeholder
        int[] toViewIDs = new int[]{R.id.date_goal_added, R.id.goal_name, R.id.current_progress_current, R.id.current_progress_total, R.id.goal_complete_check}; // Placeholder
        // Set up the adapter
        myCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.custom_row, cursor, fromFieldNames, toViewIDs, 0);
        myList = (ListView) v.findViewById(R.id.history_list); // get list view into main activity
        myCursorAdapter.changeCursor(db.getAllRows());
        myList.setAdapter(myCursorAdapter);
        myList.setFocusable(false);
        setListViewHeightBasedOnItems(myList);
        return v;
    }

    public void populateSpinner(Spinner spinner, String[] ddInfo) {
        // Basically the same as using a listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, ddInfo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
