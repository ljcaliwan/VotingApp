package com.project.votingapp.Activities.AdminFragments.TallyBoard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.project.votingapp.Activities.Model.CandidatesData;
import com.project.votingapp.Activities.ViewHolder.CandidatesViewHolder;
import com.project.votingapp.R;

import java.util.Objects;

public class TallyBoardFragment extends Fragment {

    private TallyBoardViewModel tallyBoardViewModel;
    private DatabaseReference reference;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String election_type;
    private ImageView noElectionImg;
    private TextView noElectionTxt;
    private CardView presidentTitle, presidentTable, vicePresidentTitle, vicePresidentTable, secretaryTitle;
    private CardView secretaryTable, auditorTitle, auditorTable, treasurerTitle, treasurerTable, p_i_o_Title, p_i_o_Table, peaceOfficerTitle, peaceOfficerTable;
    private CardView secondYearRepresentativeTitle, secondYearRepresentativeTable, fourthYearRepresentativeTitle, fourthYearRepresentativeTable;
    private CardView grade9ChairpersonTitle, grade9ChairpersonTable, grade10ChairpersonTitle, grade10ChairpersonTable;

    private RecyclerView presidentRecyclerView, vicePresidentRecyclerView, secretaryRecyclerView, auditorRecyclerView,
                         treasurerRecyclerView, p_i_oRecyclerView, peaceOfficerRecyclerView,
                         grade9ChairpersonRecyclerView, grade10ChairpersonRecyclerView,
                         secondYearRecyclerView, fourthYearRecyclerView;
    private FirebaseRecyclerOptions<CandidatesData> options;
    private FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        tallyBoardViewModel = ViewModelProviders.of(this).get(TallyBoardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tally_board, container, false);

        noElectionImg = root.findViewById(R.id.noNetImg);
        noElectionTxt = root.findViewById(R.id.noNetTxt);

        presidentTitle = root.findViewById(R.id.presidentTitle);
        presidentTable = root.findViewById(R.id.presidentTable);
        vicePresidentTitle = root.findViewById(R.id.vicePresidentTitle);
        vicePresidentTable = root.findViewById(R.id.vicePresidentTable);
        secretaryTitle = root.findViewById(R.id.secretaryTitle);
        secretaryTable = root.findViewById(R.id.secretaryTable);
        auditorTitle = root.findViewById(R.id.auditorTitle);
        auditorTable = root.findViewById(R.id.auditorTable);
        treasurerTitle = root.findViewById(R.id.treasurerTitle);
        treasurerTable = root.findViewById(R.id.treasurerTable);
        p_i_o_Title = root.findViewById(R.id.p_i_oTitle);
        p_i_o_Table = root.findViewById(R.id.p_i_oTable);
        peaceOfficerTitle = root.findViewById(R.id.peaceOfficerTitle);
        peaceOfficerTable = root.findViewById(R.id.peaceOfficerTable);

        grade9ChairpersonTitle = root.findViewById(R.id.grade9ChairpersonTitle);
        grade9ChairpersonTable = root.findViewById(R.id.grade9ChairpersonTable);
        grade10ChairpersonTitle = root.findViewById(R.id.grade10ChairpersonTitle);
        grade10ChairpersonTable = root.findViewById(R.id.grade10ChairpersonTable);

        secondYearRepresentativeTitle = root.findViewById(R.id.secondYearRepresentativeTitle);
        secondYearRepresentativeTable = root.findViewById(R.id.secondYearRepresentativeTable);
        fourthYearRepresentativeTitle = root.findViewById(R.id.fourthYearRepresentativeTitle);
        fourthYearRepresentativeTable = root.findViewById(R.id.fourthYearRepresentativeTable);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        checkElectionType();

        presidentRecyclerView = root.findViewById(R.id.presidentTallyBoardRecycleViewLayout);
        presidentRecyclerView.setHasFixedSize(true);
        presidentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidatePresidentExist();
        loadPresidentCandidatesData();

        vicePresidentRecyclerView = root.findViewById(R.id.vicePresidentTallyBoardRecycleViewLayout);
        vicePresidentRecyclerView.setHasFixedSize(true);
        vicePresidentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateVicePresidentExist();
        loadVicePresidentCandidatesData();

        secretaryRecyclerView = root.findViewById(R.id.secretaryTallyBoardRecycleViewLayout);
        secretaryRecyclerView.setHasFixedSize(true);
        secretaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateSecretaryExist();
        loadSecretaryCandidatesData();

        auditorRecyclerView = root.findViewById(R.id.auditorTallyBoardRecycleViewLayout);
        auditorRecyclerView.setHasFixedSize(true);
        auditorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateAuditorExist();
        loadAuditorCandidatesData();

        treasurerRecyclerView = root.findViewById(R.id.treasurerTallyBoardRecycleViewLayout);
        treasurerRecyclerView.setHasFixedSize(true);
        treasurerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateTreasurerExist();
        loadTreasurerCandidatesData();

        p_i_oRecyclerView = root.findViewById(R.id.p_i_oTallyBoardRecycleViewLayout);
        p_i_oRecyclerView.setHasFixedSize(true);
        p_i_oRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidatePIOExist();
        loadPIOCandidatesData();

        peaceOfficerRecyclerView = root.findViewById(R.id.peaceOfficerTallyBoardRecycleViewLayout);
        peaceOfficerRecyclerView.setHasFixedSize(true);
        peaceOfficerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidatePeaceOfficerExist();
        loadPeaceOfficerCandidatesData();

        grade9ChairpersonRecyclerView = root.findViewById(R.id.grade9ChairpersonTallyBoardRecycleViewLayout);
        grade9ChairpersonRecyclerView.setHasFixedSize(true);
        grade9ChairpersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateGrade9Exist();
        loadGrade9CandidatesData();

        grade10ChairpersonRecyclerView = root.findViewById(R.id.grade10ChairpersonTallyBoardRecycleViewLayout);
        grade10ChairpersonRecyclerView.setHasFixedSize(true);
        grade10ChairpersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateGrade10Exist();
        loadGrade10CandidatesData();

        secondYearRecyclerView = root.findViewById(R.id.secondYearRepresentativeTallyBoardRecycleViewLayout);
        secondYearRecyclerView.setHasFixedSize(true);
        secondYearRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateSecondYearExist();
        loadSecondYearCandidatesData();

        fourthYearRecyclerView = root.findViewById(R.id.fourthYearRepresentativeTallyBoardRecycleViewLayout);
        fourthYearRecyclerView.setHasFixedSize(true);
        fourthYearRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfCandidateFourthYearExist();
        loadFourthYearCandidatesData();

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

                        presidentTitle.setVisibility(View.VISIBLE);
                        presidentTable.setVisibility(View.VISIBLE);
                        vicePresidentTitle.setVisibility(View.VISIBLE);
                        vicePresidentTable.setVisibility(View.VISIBLE);
                        secretaryTitle.setVisibility(View.VISIBLE);
                        secretaryTable.setVisibility(View.VISIBLE);
                        auditorTitle.setVisibility(View.VISIBLE);
                        auditorTable.setVisibility(View.VISIBLE);
                        treasurerTitle.setVisibility(View.VISIBLE);
                        treasurerTable.setVisibility(View.VISIBLE);
                        p_i_o_Title.setVisibility(View.VISIBLE);
                        p_i_o_Table.setVisibility(View.VISIBLE);
                        peaceOfficerTitle.setVisibility(View.VISIBLE);
                        peaceOfficerTable.setVisibility(View.VISIBLE);

                        grade9ChairpersonTitle.setVisibility(View.GONE);
                        grade9ChairpersonTable.setVisibility(View.GONE);
                        grade10ChairpersonTitle.setVisibility(View.GONE);
                        grade10ChairpersonTable.setVisibility(View.GONE);

                        noElectionImg.setVisibility(View.GONE);
                        noElectionTxt.setVisibility(View.GONE);

                        secondYearRepresentativeTitle.setVisibility(View.VISIBLE);
                        secondYearRepresentativeTable.setVisibility(View.VISIBLE);
                        fourthYearRepresentativeTitle.setVisibility(View.VISIBLE);
                        fourthYearRepresentativeTable.setVisibility(View.VISIBLE);

                    } else {
                        presidentTitle.setVisibility(View.VISIBLE);
                        presidentTable.setVisibility(View.VISIBLE);
                        vicePresidentTitle.setVisibility(View.VISIBLE);
                        vicePresidentTable.setVisibility(View.VISIBLE);
                        secretaryTitle.setVisibility(View.VISIBLE);
                        secretaryTable.setVisibility(View.VISIBLE);
                        auditorTitle.setVisibility(View.VISIBLE);
                        auditorTable.setVisibility(View.VISIBLE);
                        treasurerTitle.setVisibility(View.VISIBLE);
                        treasurerTable.setVisibility(View.VISIBLE);
                        p_i_o_Title.setVisibility(View.VISIBLE);
                        p_i_o_Table.setVisibility(View.VISIBLE);
                        peaceOfficerTitle.setVisibility(View.VISIBLE);
                        peaceOfficerTable.setVisibility(View.VISIBLE);

                        grade9ChairpersonTitle.setVisibility(View.VISIBLE);
                        grade9ChairpersonTable.setVisibility(View.VISIBLE);
                        grade10ChairpersonTitle.setVisibility(View.VISIBLE);
                        grade10ChairpersonTable.setVisibility(View.VISIBLE);

                        noElectionImg.setVisibility(View.GONE);
                        noElectionTxt.setVisibility(View.GONE);

                        secondYearRepresentativeTitle.setVisibility(View.GONE);
                        secondYearRepresentativeTable.setVisibility(View.GONE);
                        fourthYearRepresentativeTitle.setVisibility(View.GONE);
                        fourthYearRepresentativeTable.setVisibility(View.GONE);
                    }
                } else {
                    presidentTitle.setVisibility(View.GONE);
                    presidentTable.setVisibility(View.GONE);
                    vicePresidentTitle.setVisibility(View.GONE);
                    vicePresidentTable.setVisibility(View.GONE);
                    secretaryTitle.setVisibility(View.GONE);
                    secretaryTable.setVisibility(View.GONE);
                    auditorTitle.setVisibility(View.GONE);
                    auditorTable.setVisibility(View.GONE);
                    treasurerTitle.setVisibility(View.GONE);
                    treasurerTable.setVisibility(View.GONE);
                    p_i_o_Title.setVisibility(View.GONE);
                    p_i_o_Table.setVisibility(View.GONE);
                    peaceOfficerTitle.setVisibility(View.GONE);
                    peaceOfficerTable.setVisibility(View.GONE);

                    noElectionImg.setVisibility(View.VISIBLE);
                    noElectionTxt.setVisibility(View.VISIBLE);
                    noElectionTxt.setText("Election Not Started yet.");
                    Toast.makeText(getActivity(), "Election not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidatePresidentExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("President");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    presidentTitle.setVisibility(View.VISIBLE);
                    presidentTable.setVisibility(View.VISIBLE);
                } else {
                    presidentTitle.setVisibility(View.GONE);
                    presidentTable.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateVicePresidentExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Vice President");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    vicePresidentTitle.setVisibility(View.VISIBLE);
                    vicePresidentTable.setVisibility(View.VISIBLE);
                } else {
                    vicePresidentTitle.setVisibility(View.GONE);
                    vicePresidentTable.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateSecretaryExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Secretary");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    secretaryTitle.setVisibility(View.VISIBLE);
                    secretaryTable.setVisibility(View.VISIBLE);
                } else {
                    secretaryTitle.setVisibility(View.GONE);
                    secretaryTable.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateAuditorExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Auditor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    auditorTitle.setVisibility(View.VISIBLE);
                    auditorTable.setVisibility(View.VISIBLE);
                } else {
                    auditorTitle.setVisibility(View.GONE);
                    auditorTable.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateTreasurerExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Treasurer");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    treasurerTitle.setVisibility(View.VISIBLE);
                    treasurerTable.setVisibility(View.VISIBLE);
                } else {
                    treasurerTitle.setVisibility(View.GONE);
                    treasurerTable.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidatePIOExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Public Information Officer");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    p_i_o_Title.setVisibility(View.VISIBLE);
                    p_i_o_Table.setVisibility(View.VISIBLE);
                } else {
                    p_i_o_Title.setVisibility(View.GONE);
                    p_i_o_Table.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidatePeaceOfficerExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Peace Officer");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    peaceOfficerTitle.setVisibility(View.VISIBLE);
                    peaceOfficerTable.setVisibility(View.VISIBLE);
                } else {
                    peaceOfficerTitle.setVisibility(View.GONE);
                    peaceOfficerTable.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateGrade9Exist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("G9 Chairperson");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    grade9ChairpersonTitle.setVisibility(View.VISIBLE);
                    grade9ChairpersonTable.setVisibility(View.VISIBLE);
                } else {
                    grade9ChairpersonTitle.setVisibility(View.GONE);
                    grade9ChairpersonTable.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateGrade10Exist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("G10 Chairperson");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    grade10ChairpersonTitle.setVisibility(View.VISIBLE);
                    grade10ChairpersonTable.setVisibility(View.VISIBLE);
                } else {
                    grade10ChairpersonTitle.setVisibility(View.GONE);
                    grade10ChairpersonTable.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateSecondYearExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("2nd Year Representative");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    secondYearRepresentativeTitle.setVisibility(View.VISIBLE);
                    secondYearRepresentativeTable.setVisibility(View.VISIBLE);
                } else {
                    secondYearRepresentativeTitle.setVisibility(View.GONE);
                    secondYearRepresentativeTable.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfCandidateFourthYearExist(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("4th Year Representative");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    fourthYearRepresentativeTitle.setVisibility(View.VISIBLE);
                    fourthYearRepresentativeTable.setVisibility(View.VISIBLE);
                } else {
                    fourthYearRepresentativeTitle.setVisibility(View.GONE);
                    fourthYearRepresentativeTable.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPresidentCandidatesData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("President");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.president_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.president_profile.getContext()).load(model.getImgID()).into(holder.president_profile);
                holder.president_name.setText(model.getCandidatesName());
                holder.president_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "President Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_president_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        presidentRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void loadVicePresidentCandidatesData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Vice President");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.vice_president_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.vice_president_profile.getContext()).load(model.getImgID()).into(holder.vice_president_profile);
                holder.vice_president_name.setText(model.getCandidatesName());
                holder.vice_president_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Vice President Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_vicepresident_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        vicePresidentRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void loadSecretaryCandidatesData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Secretary");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.secretary_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.secretary_profile.getContext()).load(model.getImgID()).into(holder.secretary_profile);
                holder.secretary_name.setText(model.getCandidatesName());
                holder.secretary_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Secretary Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_secretary_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        secretaryRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadAuditorCandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Auditor");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.auditor_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.auditor_profile.getContext()).load(model.getImgID()).into(holder.auditor_profile);
                holder.auditor_name.setText(model.getCandidatesName());
                holder.auditor_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Auditor Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_auditor_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        auditorRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadTreasurerCandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Treasurer");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.treasurer_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.treasurer_profile.getContext()).load(model.getImgID()).into(holder.treasurer_profile);
                holder.treasurer_name.setText(model.getCandidatesName());
                holder.treasurer_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Treasurer Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_treasurer_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        treasurerRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void loadPIOCandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Public Information Officer");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.p_i_o_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.p_i_o_profile.getContext()).load(model.getImgID()).into(holder.p_i_o_profile);
                holder.p_i_o_name.setText(model.getCandidatesName());
                holder.p_i_o_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "P.I.O Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_p_i_o_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        p_i_oRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadPeaceOfficerCandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Peace Officer");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.peace_officer_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.peace_officer_profile.getContext()).load(model.getImgID()).into(holder.peace_officer_profile);
                holder.peace_officer_name.setText(model.getCandidatesName());
                holder.peace_officer_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Peace Officer Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_peace_officer_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        peaceOfficerRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadGrade9CandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("G9 Chairperson");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.grade9_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.grade9_profile.getContext()).load(model.getImgID()).into(holder.grade9_profile);
                holder.grade9_name.setText(model.getCandidatesName());
                holder.grade9_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Grade 9 Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }
            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_grade9_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        grade9ChairpersonRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadGrade10CandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("G10 Chairperson");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.grade10_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.grade10_profile.getContext()).load(model.getImgID()).into(holder.grade10_profile);
                holder.grade10_name.setText(model.getCandidatesName());
                holder.grade10_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Grade 10 Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_grade10_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        grade10ChairpersonRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadSecondYearCandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("2nd Year Representative ");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.second_year_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.second_year_profile.getContext()).load(model.getImgID()).into(holder.second_year_profile);
                holder.second_year_name.setText(model.getCandidatesName());
                holder.second_year_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Second Year Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_secondyear_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        secondYearRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadFourthYearCandidatesData() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("4th Year Representative ");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.fourth_year_partylist.setText(model.getCandidatesPartyList());
                Glide.with(holder.fourth_year_profile.getContext()).load(model.getImgID()).into(holder.fourth_year_profile);
                holder.fourth_year_name.setText(model.getCandidatesName());
                holder.fourth_year_votes.setText(String.valueOf(model.getCandidatesVotes()));
                Toast.makeText(getContext(), "Fourth Year Candidate data loaded successful", Toast.LENGTH_LONG).show();

            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tallyboard_fourthyear_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        fourthYearRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}