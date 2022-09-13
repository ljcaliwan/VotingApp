package com.project.votingapp.Activities.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.votingapp.R;

public class VotersViewHolder extends RecyclerView.ViewHolder {

    public TextView voter_name, voter_ID, voter_yearLevel, voter_section;

    public VotersViewHolder(@NonNull View itemView) {
        super(itemView);
        voter_name = itemView.findViewById(R.id.voterFullName);
        voter_ID = itemView.findViewById(R.id.studIDOrLRN);
        voter_yearLevel = itemView.findViewById(R.id.gradeLevel);
        voter_section = itemView.findViewById(R.id.voterSection);
    }
}
