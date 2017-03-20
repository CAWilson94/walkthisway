package Tabs;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.charl.walkthisway.MainActivity;
import com.example.charl.walkthisway.Populate;
import com.example.charl.walkthisway.R;

import Dialogs.ClearHistoryWarning;
import Dialogs.CreateNewGoal;
import Models.DbManager;

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


    private Cursor cursor;
    CardView cardListView;
    String time[] = {"Day view", "Week view", "Month View", "Custom View"};
    String units[] = {"Steps", "Km", "Miles", "Monroes.."};
    String complete[] = {"Complete", "All"};
    private View v;
    private SimpleCursorAdapter myCursorAdapter;

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
        DbManager db = new DbManager(this.getActivity(), null, null, DbManager.DATABASE_VERSION);
    }

    @Override
    public void onPause() {
        populateListView();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "on RESUME HAS BEEN CALLED -------------------------------");
        populateListView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_test, container, false);
        CardView cardView = (CardView) v.findViewById(R.id.card_history);
        Spinner spinnerCompleteView = (Spinner) v.findViewById(R.id.spinner_complete);
        Spinner spinnerTimeView = (Spinner) v.findViewById(R.id.spinner_time);
        Spinner spinnerUnitsView = (Spinner) v.findViewById(R.id.spinner_units);
        pop.populateSpinner(spinnerTimeView, time, getContext());
        pop.populateSpinner(spinnerUnitsView, units, getContext());
        pop.populateSpinner(spinnerCompleteView, complete, getContext());

        //spinnerCompleteView.getOnItemSelectedListener(new AdapterView.OnItemSelectedListener()})

        String text = spinnerCompleteView.getSelectedItem().toString();

        populateListView();
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
        ListView myList;
        DbManager db = new DbManager(this.getActivity(), null, null, DbManager.DATABASE_VERSION);
        cursor = db.simpleHistory();
        String[] fromFieldNames = new String[]{db.COLUMN_DATE_GOALS, db.COLUMN_GOAL_NAME,
                db.COLUMN_CURRENT_STEPS, db.COLUMN_STEP_GOALS, db.COLUMN_GOAL_COMPLETE}; // Placeholder

        int[] toViewIDs = new int[]{R.id.date_goal_added, R.id.goal_name, R.id.current_progress_current, R.id.current_progress_total, R.id.goal_complete_check}; // Placeholder
        // Set up the adapter
        myCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.custom_row, cursor, fromFieldNames, toViewIDs, 0);
        myList = (ListView) v.findViewById(R.id.history_list); // get list view into main activity
        myCursorAdapter.changeCursor(db.simpleHistory());
        myList.setAdapter(myCursorAdapter);
        myList.setFocusable(false);
        setListViewHeightBasedOnItems(myList, cardListView);
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_delete_history);
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
