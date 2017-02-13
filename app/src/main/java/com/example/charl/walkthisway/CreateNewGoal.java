package com.example.charl.walkthisway;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import Models.DbManager;
import Models.Goals;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement thee
 * to handle interaction events.
 * Use the {@link CreateNewGoal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewGoal extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CreateNewGoalDialogListener mcallBack;

    // Custom Dialog fields
    View v;
    EditText goalNameInput, stepsInput;
    Bundle bundle = new Bundle();

    public CreateNewGoal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewGoal.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewGoal newInstance(String param1, String param2) {
        CreateNewGoal fragment = new CreateNewGoal();
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
        v = inflater.inflate(R.layout.fragment_create_new_goal, null);

        // TODO: change theme of overlay
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set custom_row inflater

        builder.setMessage("Input Goal Info");

        builder.setView(v)
                // Action Buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbManager db;
                        db = new DbManager(getActivity(), null, null, 2); // let the dbmanager take care of params

                        goalNameInput = (EditText) v.findViewById(R.id.goal_form);
                        stepsInput = (EditText) v.findViewById(R.id.goal_step_form);
                        Goals goal = new Goals();
                        goal.setName(goalNameInput.getText().toString());
                        goal.setStepTarget(Integer.valueOf(stepsInput.getText().toString()));
                        // now check toggle thingy
                        goal.setActive(true);
                        goal.setComplete(false);
                        goal.setNumSteps(0);
                        db.addGoal(goal);
                        // Need to add update list somehow..
                        //MainActivity activity = (MainActivity) getActivity();
                        //activity.onGoalAdd();
                        // mcallBack.updateList()
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }


    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_new_goal, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


    @Override
    public void onDetach() {
        super.onDetach();
        mcallBack = null;
    }

    public void setListener(CreateNewGoalDialogListener callback) {
        try {
            mcallBack = callback;
        } catch (ClassCastException e) {
            throw new ClassCastException(callback.toString() + " must implementtene CreateNewGoalDialogListener");
        }
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
    public interface CreateNewGoalDialogListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void updateList();
    }


}
