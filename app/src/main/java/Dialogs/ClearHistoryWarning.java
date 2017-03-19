package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.charl.walkthisway.R;

import Models.DbManager;


public class ClearHistoryWarning extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ClearHistoryWarning.OnCompleteListener completeListener;
    DbManager db = new DbManager(this.getActivity(), null, null, DbManager.DATABASE_VERSION);

    public ClearHistoryWarning() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClearHistoryWarning.
     */
    // TODO: Rename and change types and number of parameters
    public static ClearHistoryWarning newInstance(String param1, String param2) {
        ClearHistoryWarning fragment = new ClearHistoryWarning();
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
        new DbManager(this.getActivity(), null, null, DbManager.DATABASE_VERSION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_clear_history_warning, null);

        builder.setView(v).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "YOU DELETED ME WHYYY", Toast.LENGTH_LONG);
                //getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
                //completeListener.onClearHistory(true);
                //db.clearHistory();
                //db.minStat();
                dismiss();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "YOU SAVE ME WHOOOO", Toast.LENGTH_LONG);
                dismiss();
            }
        });
        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        completeListener = null;
    }

    public static interface OnCompleteListener {
        public abstract void onClearHistory(Boolean clearHistory);
    }

    // make sure the Activity implemented it


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.completeListener = (ClearHistoryWarning.OnCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }

    }


}
