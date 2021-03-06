package Tabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charl.walkthisway.Populate;
import com.example.charl.walkthisway.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Models.DbManager;
import SystemDateStrategy.SystemDatePreferenceManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Statistics.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DbManager dbMan;
    private Populate pop = new Populate();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SystemDatePreferenceManager us = new SystemDatePreferenceManager();

    private OnFragmentInteractionListener mListener;
    View v;
    TextView averageStatsText, maxStatsText, minStatsText;

    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    String time[] = {"Day view", "Week view", "Month View", "Custom View"};
    String units[] = {"Steps", "Km", "Miles", "Monroes.."};
    String complete[] = {"Complete", "All"};

    // line chart shit
    List<Entry> entries = new ArrayList<Entry>();

    public Statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Statistics.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistics newInstance(String param1, String param2) {
        Statistics fragment = new Statistics();
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
        db = new DbManager(getActivity(), null, null, 2);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history, container, false);
        final CardView cardView = (CardView) v.findViewById(R.id.card_stats);
        LineChart chart = (LineChart) v.findViewById(R.id.chart);
        averageStatsText = (TextView) v.findViewById(R.id.average_stats_text);
        maxStatsText = (TextView) v.findViewById(R.id.max_stats_text);
        minStatsText = (TextView) v.findViewById(R.id.min_stats_text);

        averageStatsText.setText("Average:  " + db.averageStat());
        maxStatsText.setText("Max:          " + db.maxStat());
        minStatsText.setText("Min:          " + db.minStat());

        Spinner spinnerCompleteView = (Spinner) v.findViewById(R.id.spinner_complete_stats);
        Spinner spinnerTimeView = (Spinner) v.findViewById(R.id.spinner_time_stats);
        Spinner spinnerUnitsView = (Spinner) v.findViewById(R.id.spinner_units_stats);


        pop.populateSpinner(spinnerTimeView, time, getContext());
        pop.populateSpinner(spinnerUnitsView, units, getContext());
        pop.populateSpinner(spinnerCompleteView, complete, getContext());
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
    }

}
