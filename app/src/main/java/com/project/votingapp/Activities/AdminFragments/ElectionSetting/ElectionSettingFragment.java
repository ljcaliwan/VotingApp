package com.project.votingapp.Activities.AdminFragments.ElectionSetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.Activities.AdminActivities.CreateElectionActivity;
import com.project.votingapp.R;

import java.util.Objects;

public class ElectionSettingFragment extends Fragment {

    private ElectionSettingViewModel electionSettingViewModel;
    private TextView electionTitle, electionCode, electionType;

    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        electionSettingViewModel = ViewModelProviders.of(this).get(ElectionSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_election_setting, container, false);

        electionTitle = root.findViewById(R.id.txt_electionTitle);
        electionCode = root.findViewById(R.id.txt_electionCode);
        electionType = root.findViewById(R.id.txt_electionType);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        try {
            ref = FirebaseDatabase.getInstance().getReference("Election List").child("Admin " + currentUser.getDisplayName() + " ELECTION");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String election_name = Objects.requireNonNull(snapshot.child("electionName").getValue()).toString();
                        String election_code = Objects.requireNonNull(snapshot.child("electionCode").getValue()).toString();
                        String election_type = Objects.requireNonNull(snapshot.child("electionType").getValue()).toString();
                        electionTitle.setText(String.format("Election Name: %s", election_name));
                        electionCode.setText(String.format("Election Code: %s", election_code));
                        electionType.setText(String.format("Election Type: %s", election_type));
                    }else {
                        Toast.makeText(getContext(), "Cannot load election settings data :(", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception ex){
            Toast.makeText(getContext(), "Cannot load election settings data :(", Toast.LENGTH_LONG).show();
        }

        return root;
    }
}