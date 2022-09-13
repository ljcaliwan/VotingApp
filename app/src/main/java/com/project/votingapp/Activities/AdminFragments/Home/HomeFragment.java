package com.project.votingapp.Activities.AdminFragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.Activities.AdminActivities.CreateElectionActivity;
import com.project.votingapp.Activities.Model.CandidatesData;
import com.project.votingapp.Activities.ViewHolder.CandidatesViewHolder;
import com.project.votingapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private FloatingActionButton fab1;
    private TextView noOfCandidates, noOfPosition, noOfVoters, noOfVotersVoted;
    private FirebaseRecyclerOptions<CandidatesData> options;
    private FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder> adapter;
    private RecyclerView presidentRecyclerView, vicePresidentRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fab1 = root.findViewById(R.id.fab1);
        noOfVoters = root.findViewById(R.id.noOfVoters);
        noOfVotersVoted = root.findViewById(R.id.noOfVotersVoted);
        noOfCandidates = root.findViewById(R.id.noOfCandidates);
        noOfPosition = root.findViewById(R.id.noOfPosition);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        presidentRecyclerView = root.findViewById(R.id.homeTallyVotersPresident);
        presidentRecyclerView.setHasFixedSize(true);
        presidentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showPresidentTallyVotes();

        vicePresidentRecyclerView = root.findViewById(R.id.homeTallyVotersVicePresident);
        vicePresidentRecyclerView.setHasFixedSize(true);
        vicePresidentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showVicePresidentTallyVotes();

        showNoOfVoters();
        showNoOfVotersVoted();
        showNoOfCandidates();
        showNoOfCandidatesPosition();

        if (CreateElectionActivity.isCreated) {
            fab1.hide();
        } else {
            checkElectionStatus();
        }

        return root;

    }

    private void showNoOfVoters() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " VOTERS");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numOfVoters = (int) snapshot.getChildrenCount();
                noOfVoters.setText(String.valueOf(numOfVoters));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showNoOfVotersVoted() {



    }

    private void showNoOfCandidates() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numOfCandidates = (int) snapshot.getChildrenCount();
                noOfCandidates.setText(String.valueOf(numOfCandidates));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showNoOfCandidatesPosition() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Position");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numOfCandidatesPosition = (int) snapshot.getChildrenCount();
                noOfPosition.setText(String.valueOf(numOfCandidatesPosition));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showPresidentTallyVotes(){

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("President");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.home_president_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.home_president_profile.getContext()).load(model.getImgID()).into(holder.home_president_profile);
                holder.home_president_name.setText(model.getCandidatesName());
                holder.home_president_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "President Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_tallyvotes_president_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        presidentRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showVicePresidentTallyVotes(){

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Vice President");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.home_vice_president_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.home_vice_president_profile.getContext()).load(model.getImgID()).into(holder.home_vice_president_profile);
                holder.home_vice_president_name.setText(model.getCandidatesName());
                holder.home_vice_president_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Vice President Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_tallyvotes_vicepresident_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        vicePresidentRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void checkElectionStatus() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List").child("Admin " + currentUser.getDisplayName() + " ELECTION");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fab1.hide();
                    fab1.setEnabled(false);
                } else {
                    fab1.show();
                    fab1.setEnabled(true);
                    fab1.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreateElectionActivity.class)));
                    Toast.makeText(getContext(), "There is no election in this account yet", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}