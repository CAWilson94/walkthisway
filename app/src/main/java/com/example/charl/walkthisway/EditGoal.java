package com.example.charl.walkthisway;

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
import android.widget.EditText;
import android.widget.Switch;

import Models.DbManager;
import Models.Goals;

import static Models.DbManager.COLUMN_GOAL_NAME;
import static Models.DbManager.COLUMN_STEP_GOALS;
import static android.os.Build.ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditGoal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditGoal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGoal extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DbManager db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

    View v;


    EditText goalNameEdit, goalStepEdit;

    private OnFragmentInteractionListener mListener;

    public EditGoal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditGoal.
     */
    // TODO: Rename and change types and number of parameters
    public static EditGoal newInstance(String param1, String param2) {
        EditGoal fragment = new EditGoal();
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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.fragment_edit_goals, null);

        // TODO: change theme of overlay
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set custom_row inflater

        DbManager db;
        db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);

        builder.setMessage("Edit yer goal fatty!");

        goalNameEdit = (EditText) v.findViewById(R.id.goal_form_edit);
        goalStepEdit = (EditText) v.findViewById(R.id.step_edit__form);

        Bundle boop = getArguments();
        final String tableID = boop.getString("IDYAS");

        goalNameEdit.setText(db.getGoalName(Integer.valueOf(tableID)));
        String stepGOAL = db.displayFieldFromID(COLUMN_STEP_GOALS, Integer.valueOf(tableID));
        goalStepEdit.setText(stepGOAL);

        builder.setView(v)
                // Action Buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbManager db;
                        db = new DbManager(getActivity(), null, null, DbManager.DATABASE_VERSION);
                        String goalName = goalNameEdit.getText().toString();
                        String stepGoal = goalStepEdit.getText().toString();
                        db.updateFieldFromID(COLUMN_GOAL_NAME, goalName, Integer.valueOf(tableID));
                        db.updateFieldFromID(COLUMN_STEP_GOALS, stepGoal, Integer.valueOf(tableID));
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
