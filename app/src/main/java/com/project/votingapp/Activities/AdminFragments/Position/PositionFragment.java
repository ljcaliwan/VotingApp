package com.project.votingapp.Activities.AdminFragments.Position;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.R;

import java.util.Objects;

public class PositionFragment extends Fragment {

    private PositionViewModel positionViewModel;
    private EditText h_txtAddPosition, c_txtAddPosition, h_txtAddPartylist, c_txtAddPartylist;
    private Button h_btnAddPosition, c_btnAddPosition, h_btnAddPartylist, c_btnAddPartylist;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String election_type;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        positionViewModel = ViewModelProviders.of(this).get(PositionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_position, container, false);

        h_txtAddPartylist = root.findViewById(R.id.h_txtAddPartylist);
        h_btnAddPartylist = root.findViewById(R.id.h_btnAddPartylist);

        c_txtAddPartylist = root.findViewById(R.id.c_txtAddPartylist);
        c_btnAddPartylist = root.findViewById(R.id.c_btnAddPartylist);

        h_txtAddPosition = root.findViewById(R.id.h_txtAddPosition);
        h_btnAddPosition = root.findViewById(R.id.h_btnAddPosition);

        c_txtAddPosition = root.findViewById(R.id.c_txtAddPosition);
        c_btnAddPosition = root.findViewById(R.id.c_btnAddPosition);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        checkElectionType();

        return root;
    }

    private void checkElectionType() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List").child("Admin " + currentUser.getDisplayName() + " ELECTION");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    election_type = Objects.requireNonNull(snapshot.child("electionType").getValue()).toString();
                    if (election_type.equalsIgnoreCase("College Election (SSC ELECTION)")) {
                        c_txtAddPartylist.setVisibility(View.VISIBLE);
                        c_btnAddPartylist.setVisibility(View.VISIBLE);
                        c_txtAddPosition.setVisibility(View.VISIBLE);
                        c_btnAddPosition.setVisibility(View.VISIBLE);
                        h_txtAddPartylist.setVisibility(View.GONE);
                        h_btnAddPartylist.setVisibility(View.GONE);
                        h_txtAddPosition.setVisibility(View.GONE);
                        h_btnAddPosition.setVisibility(View.GONE);
                        addCollegePartylist();
                        addCollegePosition();
                    } else {
                        h_txtAddPartylist.setVisibility(View.VISIBLE);
                        h_btnAddPartylist.setVisibility(View.VISIBLE);
                        h_txtAddPosition.setVisibility(View.VISIBLE);
                        h_btnAddPosition.setVisibility(View.VISIBLE);
                        c_txtAddPartylist.setVisibility(View.GONE);
                        c_btnAddPartylist.setVisibility(View.GONE);
                        c_txtAddPosition.setVisibility(View.GONE);
                        c_btnAddPosition.setVisibility(View.GONE);
                        addHighSchoolPartylist();
                        addHighSchoolPosition();

                    }
                } else {
                    Toast.makeText(getActivity(), "Election not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addHighSchoolPartylist() {
        h_btnAddPartylist.setOnClickListener(v -> {
            String txtData = h_txtAddPartylist.getText().toString().trim();
            if (txtData.isEmpty()) {
                h_txtAddPartylist.setError("Please enter candidate partylist");
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Partylist");
                reference.push().setValue(txtData).addOnCompleteListener(task -> {
                    h_txtAddPartylist.setText("");
                    Toast.makeText(getActivity(), "Partylist added", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void addCollegePartylist() {
        c_btnAddPartylist.setOnClickListener(v -> {
            String txtData = c_txtAddPartylist.getText().toString().trim();
            if (txtData.isEmpty()) {
                c_txtAddPartylist.setError("Please enter candidate partylist");
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Partylist");
                reference.push().setValue(txtData).addOnCompleteListener(task -> {
                    c_txtAddPartylist.setText("");
                    Toast.makeText(getActivity(), "Partylist added", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void addHighSchoolPosition() {
        h_btnAddPosition.setOnClickListener(v -> {
            String txtData = h_txtAddPosition.getText().toString().trim();
            if (txtData.isEmpty()) {
                h_txtAddPosition.setError("Please enter candidate position");
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Position");
                reference.push().setValue(txtData).addOnCompleteListener(task -> {
                    h_txtAddPosition.setText("");
                    Toast.makeText(getActivity(), "position Added", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void addCollegePosition() {
        c_btnAddPosition.setOnClickListener(v -> {
            String txtData = c_txtAddPosition.getText().toString().trim();
            if (txtData.isEmpty()) {
                c_txtAddPosition.setError("Please enter candidate position");
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Position");
                reference.push().setValue(txtData).addOnCompleteListener(task -> {
                    c_txtAddPosition.setText("");
                    Toast.makeText(getActivity(), "position Added", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

}