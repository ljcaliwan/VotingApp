package com.project.votingapp.Activities.AdminActivities.Classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.project.votingapp.R;

public class CreateElectionDialog extends AppCompatDialogFragment {

    private EditText electionName;
    private CreateElectionDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_election_popup, null);

        builder.setView(view).setTitle("Election Title").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleName = electionName.getText().toString();
                listener.setTitleName(titleName);
            }
        });

        electionName = view.findViewById(R.id.txt_createElection);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CreateElectionDialogListener)context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "must implement CreateElectionDialogListener");
        }

    }
    public  interface CreateElectionDialogListener{
        void setTitleName(String titleName);
    }

}
