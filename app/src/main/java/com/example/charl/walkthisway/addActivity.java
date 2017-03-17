package com.example.charl.walkthisway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;

import Models.DbManager;

import static Models.DbManager.COLUMN_DATE_GOALS;
import static java.security.AccessController.getContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link addActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link addActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addActivity extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View v;
    EditText stepsInput;

    private OnFragmentInteractionListener mListener;
    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    public addActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static addActivity newInstance(String param1, String param2) {
        addActivity fragment = new addActivity();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // TODO: change theme of overlay
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set custom_row inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.fragment_add_activity, null);

        builder.setMessage("Input Steps");

        builder.setView(v)
                // Action Buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbManager db;
                        db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);
                        stepsInput = (EditText) v.findViewById(R.id.add_steps_input);
                        String steps = stepsInput.getText().toString();
                        Date currentDate = new Date(System.currentTimeMillis());
                        int stepInt = Integer.parseInt(steps);
                        db.incrementSteps(stepInt, currentDate);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
