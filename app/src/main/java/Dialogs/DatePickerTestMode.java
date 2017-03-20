package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.charl.walkthisway.R;

import Models.DbManager;
import SystemDateStrategy.SystemDateManager;
import SystemDateStrategy.SystemDatePreferenceManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatePickerTestMode extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SystemDatePreferenceManager util = new SystemDatePreferenceManager();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String dateString;
    Date someDate;
    Boolean datePicked = false;
    private OnCompleteListener completeListener;
    DbManager db;


    public DatePickerTestMode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatePickerTestMode.
     */
    // TODO: Rename and change types and number of parameters
    public static DatePickerTestMode newInstance(String param1, String param2) {
        DatePickerTestMode fragment = new DatePickerTestMode();
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
        db = new DbManager(this.getActivity().getApplicationContext(), null, null, DbManager.DATABASE_VERSION);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_date_picker, null);

        final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();


        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        if (!datePicker.equals(null)) {
                            someDate = new Date(year - 1900, month, dayOfMonth);

                            util.testModeDate(someDate, getContext());

                            SystemDateManager date = new SystemDateManager();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            // dateString = sdf.format(someDate);
                            dateString = date.systemDateDecider(getContext());
                            datePicked = true;

                        }
                    }
                });

        //db.minStat();


        builder.setView(v).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), dateString, Toast.LENGTH_LONG);
                //DbManager db = new DbManager(getContext().getApplicationContext(), null, null, DbManager.DATABASE_VERSION);
                //db.minStat();
                if (datePicked) {
                    completeListener.onComplete(someDate);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
                    db.dayPassed();
                    datePicked = false;
                } else {
                    Toast.makeText(getContext(), "If no date is picked... can you really call this a date picker?", Toast.LENGTH_LONG).show();
                }

                dismiss();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "dis is cancel", Toast.LENGTH_LONG);
                dismiss();
            }
        });
        return builder.create();
    }


    public static interface OnCompleteListener {
        public abstract void onComplete(Date date);
    }

    // make sure the Activity implemented it


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.completeListener = (OnCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }
}
