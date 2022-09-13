package com.project.votingapp.Activities.AdminFragments.Voters;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.Activities.AdminActivities.AddVoterActivity;
import com.project.votingapp.Activities.ViewHolder.VotersViewHolder;
import com.project.votingapp.Activities.Model.VotersData;
import com.project.votingapp.R;

import java.util.Objects;

public class VotersFragment extends Fragment {

    private VotersViewModel votersViewModel;

    private Dialog voterDialog;
    private ImageView addVoter;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    ConstraintLayout noVotersLayout;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<VotersData> options;
    private FirebaseRecyclerAdapter<VotersData, VotersViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        votersViewModel = ViewModelProviders.of(this).get(VotersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_voters, container, false);
        setHasOptionsMenu(true);
        voterDialog = new Dialog(Objects.requireNonNull(getActivity()));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        recyclerView = root.findViewById(R.id.votersRecycleViewLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noVotersLayout = root.findViewById(R.id.noVotersLayout);

        try {
            ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                    .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                    .child("Admin " + currentUser.getDisplayName() + " VOTERS");

            loadVotersData();
        } catch (Exception ex) {
            noVotersLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Cannot load voters data :(", Toast.LENGTH_LONG).show();
        }

        return root;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_voter_toolbar_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addVoters) {
            ref = FirebaseDatabase.getInstance().getReference().child("Election List");
            ref.orderByChild("adminEmail").equalTo(currentUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //election already exist
                    if (dataSnapshot.exists()) {
                        startActivity(new Intent(getActivity(), AddVoterActivity.class));
                    } else {
                        //election not exist
                        showVoterDialog(getView());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadVotersData() {
        options = new FirebaseRecyclerOptions.Builder<VotersData>().setQuery(ref, VotersData.class).build();
        adapter = new FirebaseRecyclerAdapter<VotersData, VotersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VotersViewHolder holder, int position, @NonNull VotersData model) {
                holder.voter_name.setText(String.format("Full Name: %s", model.getVoterName()));
                holder.voter_ID.setText(String.format("LRN/Student ID: %s", model.getVoterID()));
                holder.voter_yearLevel.setText(String.format("Year/Grade Level: %s", model.getVoterYearLevel()));
                holder.voter_section.setText(String.format("Section: %s", model.getVoterSection()));
                Toast.makeText(getContext(), "Voters data loaded successfully", Toast.LENGTH_LONG).show();
            }

            @NonNull
            @Override
            public VotersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.votersrecycleviewdata, parent, false);
                return new VotersViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showVoterDialog(View view) {
        Button btnOk;
        voterDialog.setContentView(R.layout.popup_voterdialog);
        btnOk = voterDialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> voterDialog.dismiss());
        voterDialog.show();
        voterDialog.setCancelable(false);
        voterDialog.setCanceledOnTouchOutside(false);
    }

}