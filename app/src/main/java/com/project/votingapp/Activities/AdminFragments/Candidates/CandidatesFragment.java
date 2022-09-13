package com.project.votingapp.Activities.AdminFragments.Candidates;

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

import com.bumptech.glide.Glide;
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
import com.project.votingapp.Activities.AdminActivities.AddCandidatesActivity;
import com.project.votingapp.Activities.AdminActivities.AddVoterActivity;
import com.project.votingapp.Activities.AdminActivities.CreateElectionActivity;
import com.project.votingapp.Activities.Model.CandidatesData;
import com.project.votingapp.Activities.Model.VotersData;
import com.project.votingapp.Activities.ViewHolder.CandidatesViewHolder;
import com.project.votingapp.Activities.ViewHolder.VotersViewHolder;
import com.project.votingapp.R;

public class CandidatesFragment extends Fragment {

    private CandidatesViewModel candidatesViewModel;
    private Dialog voterDialog;
    ConstraintLayout noCandidatesLayout;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<CandidatesData> options;
    private FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        candidatesViewModel = ViewModelProviders.of(this).get(CandidatesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_candidates, container, false);
        setHasOptionsMenu(true);
        voterDialog = new Dialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        recyclerView = root.findViewById(R.id.candidatesRecycleViewLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noCandidatesLayout = root.findViewById(R.id.noCandidatesLayout);

        //load Candidates Data
        try {
            ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                    .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                    .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
            loadCandidatesData();
        }catch (Exception ex){
            Toast.makeText(getContext(), "Cannot load Candidates data :(", Toast.LENGTH_LONG).show();
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_candidates_toolbar_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addCandidates) {

            ref = FirebaseDatabase.getInstance().getReference().child("Election List");
            ref.orderByChild("adminEmail").equalTo(currentUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //election already exist
                    if (dataSnapshot.exists()){
                        startActivity(new Intent(getActivity(), AddCandidatesActivity.class));
                    }else{
                        //election not exist
                        showCandidatesDialog(getView());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCandidatesData(){
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(ref, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.candidates_ID.setText(String.format("LRN/Student ID: %s", model.getCandidatesID()));
                holder.candidates_name.setText(String.format("Full Name: %s", model.getCandidatesName()));
                holder.candidates_partyList.setText(String.format("Partylist: %s", model.getCandidatesPartyList()));
                holder.candidates_yearLevel.setText(String.format("Year/Grade Level: %s", model.getCandidatesYearLevel()));
                holder.candidates_section.setText(String.format("Section: %s", model.getCandidatesSection()));
                holder.candidates_positionRunningFor.setText(String.format("Position: %s", model.getCandidatesPositionRunningFor()));
                Glide.with(holder.candidates_profile.getContext()).load(model.getImgID()).into(holder.candidates_profile);
                Toast.makeText(getContext(), "Candidates data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidatesrecycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showCandidatesDialog(View view){
        Button btnOk;
        voterDialog.setContentView(R.layout.popup_candidatesdialog);
        btnOk = voterDialog.findViewById(R.id.btnOk2);
        btnOk.setOnClickListener(v -> voterDialog.dismiss());
        voterDialog.show();
        voterDialog.setCancelable(false);
        voterDialog.setCanceledOnTouchOutside(false);
    }

}