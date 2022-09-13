package com.project.votingapp.Activities.VotersActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.Activities.Model.CandidatesData;
import com.project.votingapp.Activities.ViewHolder.CandidatesViewHolder;
import com.project.votingapp.R;

public class VotersHomeActivity extends AppCompatActivity {

    private FirebaseRecyclerOptions<CandidatesData> options;
    private FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder> adapter;
    private String presidentName, vicePresidentName, secretaryName, auditorName, treasurerName, p_i_oName,
            peaceOfficerName, grade9Name, grade10Name, secondYearRepName, fourthYearRepName;
    SparseBooleanArray sparseBooleanArray;

    public VotersHomeActivity() {
        sparseBooleanArray = new SparseBooleanArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voters_home);

        showCandidatesBallot();

        Button btnSubmitVote = findViewById(R.id.submitVote);
        btnSubmitVote.setOnClickListener(v -> submitVotes());

    }

    private void showCandidatesBallot(){
        showPresidentBallot();
        showVicePresidentBallot();
        showSecretaryBallot();
        showAuditorBallot();
        showTreasurerBallot();
        showP_i_o_Ballot();
        showPeaceOfficerBallot();
        showGrade9ChairpersonBallot();
        showGrade10ChairpersonBallot();
//        showSecondYearRepresentativeBallot();
//        showFourthYearRepresentativeBallot();
    }

    private void submitVotes() {

        if (presidentName == null && vicePresidentName == null && secretaryName == null && auditorName == null && treasurerName == null
             && p_i_oName == null && peaceOfficerName == null && grade9Name == null && grade10Name == null){
            Toast.makeText(getApplicationContext(), "Please select Candidates", Toast.LENGTH_LONG).show();
        }else if (presidentName == null) {
            Toast.makeText(getApplicationContext(), "Please select President candidate", Toast.LENGTH_LONG).show();
        }else if(vicePresidentName == null){
            Toast.makeText(getApplicationContext(), "Please select Vice President candidate", Toast.LENGTH_LONG).show();
        }else if(secretaryName == null){
            Toast.makeText(getApplicationContext(), "Please select Secretary candidate", Toast.LENGTH_LONG).show();
        }else if(auditorName == null){
            Toast.makeText(getApplicationContext(), "Please select Auditor candidate", Toast.LENGTH_LONG).show();
        }else if(treasurerName == null){
            Toast.makeText(getApplicationContext(), "Please select Treasurer candidate", Toast.LENGTH_LONG).show();
        }else if(p_i_oName == null){
            Toast.makeText(getApplicationContext(), "Please select P.I.O candidate", Toast.LENGTH_LONG).show();
        }else if(peaceOfficerName == null){
            Toast.makeText(getApplicationContext(), "Please select Peace Officer candidate", Toast.LENGTH_LONG).show();
        }else if(grade9Name == null){
            Toast.makeText(getApplicationContext(), "Please select Grade 9 Chairperson candidate", Toast.LENGTH_LONG).show();
        }else if(grade10Name == null){
            Toast.makeText(getApplicationContext(), "Please select Grade 10 Chairperson candidate", Toast.LENGTH_LONG).show();
        }else{
            submitPresidentVote();
            submitVicePresidentVote();
            submitSecretaryVote();
            submitAuditorVote();
            submitTreasurerVote();
            submitPublicInfoOfficerVote();
            submitPeaceOfficerVote();
            submitGrade9ChairpersonVote();
            submitGrade10ChairpersonVote();
            //submitSecondYearRepresentativeVote();
            //submitFourthYearRepresentativeVote();

            startActivity(new Intent(getApplicationContext(), DoneVotingActivity.class));
            finish();

        }
    }

    private void submitPresidentVote() {
        DatabaseReference presidentReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("President");
        presidentReference.orderByChild("candidatesName").equalTo(presidentName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference presReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("President")
                            .child(presidentName).child("candidatesVotes");
                    presReference.runTransaction(new Transaction.Handler() { //continuously incrementing president candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            if (mutableData.getValue() == null) {
                                mutableData.setValue(0);
                            } else {
                                mutableData.setValue((long) mutableData.getValue() + 1);
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "President Vote Added", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitVicePresidentVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Vice President");
        reference.orderByChild("candidatesName").equalTo(vicePresidentName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference vicePresReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Vice President")
                            .child(vicePresidentName).child("candidatesVotes");
                    vicePresReference.runTransaction(new Transaction.Handler() { //continuously incrementing V-President candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), "There is an error incrementing data", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Vice President Vote Added", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void submitSecretaryVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Secretary");
        reference.orderByChild("candidatesName").equalTo(secretaryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference secretaryReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Secretary")
                            .child(secretaryName).child("candidatesVotes");
                    secretaryReference.runTransaction(new Transaction.Handler() { //continuously incrementing Secretary candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Secretary Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void submitAuditorVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Auditor");
        reference.orderByChild("candidatesName").equalTo(auditorName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference auditorReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Auditor")
                            .child(auditorName).child("candidatesVotes");
                    auditorReference.runTransaction(new Transaction.Handler() { //continuously incrementing Auditor candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Auditor Vote Added", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitTreasurerVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Treasurer");
        reference.orderByChild("candidatesName").equalTo(treasurerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference treasurerReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Treasurer")
                            .child(treasurerName).child("candidatesVotes");
                    treasurerReference.runTransaction(new Transaction.Handler() { //continuously incrementing Auditor candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Treasurer Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitPublicInfoOfficerVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Public Information Officer");
        reference.orderByChild("candidatesName").equalTo(p_i_oName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference p_i_oReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Public Information Officer")
                            .child(p_i_oName).child("candidatesVotes");
                    p_i_oReference.runTransaction(new Transaction.Handler() { //continuously incrementing Auditor candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }
                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "P.I.O Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitPeaceOfficerVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Peace Officer");
        reference.orderByChild("candidatesName").equalTo(peaceOfficerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference peaceOfficerReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Peace Officer")
                            .child(peaceOfficerName).child("candidatesVotes");
                    peaceOfficerReference.runTransaction(new Transaction.Handler() { //continuously incrementing Peace Officer candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Peace Officer Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitGrade9ChairpersonVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("G9 Chairperson");
        reference.orderByChild("candidatesName").equalTo(grade9Name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference grade9ChairpersonReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("G9 Chairperson")
                            .child(grade9Name).child("candidatesVotes");
                    grade9ChairpersonReference.runTransaction(new Transaction.Handler() { //continuously incrementing Grade 9 Chairperson candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Grade 9 Chairperson Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitGrade10ChairpersonVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("G10 Chairperson");
        reference.orderByChild("candidatesName").equalTo(grade10Name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference grade10ChairpersonReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("G10 Chairperson")
                            .child(grade10Name).child("candidatesVotes");
                    grade10ChairpersonReference.runTransaction(new Transaction.Handler() { //continuously incrementing Grade 10 Chairperson candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Grade 10 Chairperson Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitSecondYearRepresentativeVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("2nd Year Representative");
        reference.orderByChild("candidatesName").equalTo(secondYearRepName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference secondYearRepReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("2nd Year Representative")
                            .child(secondYearRepName).child("candidatesVotes");
                    secondYearRepReference.runTransaction(new Transaction.Handler() { //continuously incrementing Second Year Rep candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }

                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "2nd Year Representative Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitFourthYearRepresentativeVote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("4th Year Representative");
        reference.orderByChild("candidatesName").equalTo(fourthYearRepName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference fourthYearRepReference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                            .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("4th Year Representative")
                            .child(fourthYearRepName).child("candidatesVotes");
                    fourthYearRepReference.runTransaction(new Transaction.Handler() { //continuously incrementing Fourth Year Rep candidates votes
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((long) currentData.getValue() + 1);
                            }
                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "4th Year Representative Vote Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showPresidentBallot() {
        RecyclerView presidentRecyclerView = findViewById(R.id.presidentBallotRecycleViewLayout);
        presidentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        presidentRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("President");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotPresidentPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotPresidentProfile.getContext()).load(model.getImgID()).into(holder.ballotPresidentProfile);
                holder.ballotPresidentName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.presidentRadioBtn.setChecked(true);
                } else {
                    holder.presidentRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.presidentRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    //presidentRadioBtnSelectedItem = position;
                    presidentName = holder.ballotPresidentName.getText().toString();
                    Toast.makeText(getApplicationContext(), presidentName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();

                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_president_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        presidentRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showVicePresidentBallot() {
        RecyclerView vicePresidentRecyclerView = findViewById(R.id.vicePresidentBallotRecycleViewLayout);
        vicePresidentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        vicePresidentRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Vice President");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotVicePresidentPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotVicePresidentProfile.getContext()).load(model.getImgID()).into(holder.ballotVicePresidentProfile);
                holder.ballotVicePresidentName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.vicePresidentRadioBtn.setChecked(true);
                } else {
                    holder.vicePresidentRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.vicePresidentRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    vicePresidentName = holder.ballotVicePresidentName.getText().toString();
                    Toast.makeText(getApplicationContext(), vicePresidentName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_vicepresident_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        vicePresidentRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showSecretaryBallot() {
        RecyclerView secretaryRecyclerView = findViewById(R.id.secretaryBallotRecycleViewLayout);
        secretaryRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        secretaryRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Secretary");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotSecretaryPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotSecretaryProfile.getContext()).load(model.getImgID()).into(holder.ballotSecretaryProfile);
                holder.ballotSecretaryName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.secretaryRadioBtn.setChecked(true);
                } else {
                    holder.secretaryRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.secretaryRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    secretaryName = holder.ballotSecretaryName.getText().toString();
                    Toast.makeText(getApplicationContext(), secretaryName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_secretary_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        secretaryRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showAuditorBallot() {
        RecyclerView auditorRecyclerView = findViewById(R.id.auditorBallotRecycleViewLayout);
        auditorRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        auditorRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Auditor");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotAuditorPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotAuditorProfile.getContext()).load(model.getImgID()).into(holder.ballotAuditorProfile);
                holder.ballotAuditorName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.auditorRadioBtn.setChecked(true);
                } else {
                    holder.auditorRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.auditorRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    auditorName = holder.ballotAuditorName.getText().toString();
                    Toast.makeText(getApplicationContext(), auditorName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_auditor_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        auditorRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showTreasurerBallot() {
        RecyclerView treasurerRecyclerView = findViewById(R.id.treasurerBallotRecycleViewLayout);
        treasurerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        treasurerRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Treasurer");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotTreasurerPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotTreasurerProfile.getContext()).load(model.getImgID()).into(holder.ballotTreasurerProfile);
                holder.ballotTreasurerName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.treasurerRadioBtn.setChecked(true);
                } else {
                    holder.treasurerRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.treasurerRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    treasurerName = holder.ballotTreasurerName.getText().toString();
                    Toast.makeText(getApplicationContext(), treasurerName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_treasurer_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        treasurerRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showP_i_o_Ballot() {
        RecyclerView p_i_oRecyclerView = findViewById(R.id.p_i_oBallotRecycleViewLayout);
        p_i_oRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        p_i_oRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Public Information Officer");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotP_i_oPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotP_i_oProfile.getContext()).load(model.getImgID()).into(holder.ballotP_i_oProfile);
                holder.ballotP_i_oName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.p_i_oRadioBtn.setChecked(true);
                } else {
                    holder.p_i_oRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.p_i_oRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    p_i_oName = holder.ballotP_i_oName.getText().toString();
                    Toast.makeText(getApplicationContext(),p_i_oName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_p_i_o_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        p_i_oRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showPeaceOfficerBallot() {
        RecyclerView peaceOfficerRecyclerView = findViewById(R.id.peaceOfficerBallotRecycleViewLayout);
        peaceOfficerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        peaceOfficerRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("Peace Officer");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotPeaceOfficerPartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotPeaceOfficerProfile.getContext()).load(model.getImgID()).into(holder.ballotPeaceOfficerProfile);
                holder.ballotPeaceOfficerName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.peaceOfficerRadioBtn.setChecked(true);
                } else {
                    holder.peaceOfficerRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.peaceOfficerRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    peaceOfficerName = holder.ballotPeaceOfficerName.getText().toString();
                    Toast.makeText(getApplicationContext(),peaceOfficerName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_peace_officer_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        peaceOfficerRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showGrade9ChairpersonBallot() {
        RecyclerView grade9ChairpersonRecyclerView = findViewById(R.id.grade9ChairpersonBallotRecycleViewLayout);
        grade9ChairpersonRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        grade9ChairpersonRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("G9 Chairperson");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotGrade9PartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotGrade9Profile.getContext()).load(model.getImgID()).into(holder.ballotGrade9Profile);
                holder.ballotGrade9Name.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.grade9RadioBtn.setChecked(true);
                } else {
                    holder.grade9RadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.grade9RadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    grade9Name = holder.ballotGrade9Name.getText().toString();
                    Toast.makeText(getApplicationContext(),grade9Name, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_grade_9_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        grade9ChairpersonRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showGrade10ChairpersonBallot() {
        RecyclerView grade10ChairpersonRecyclerView = findViewById(R.id.grade10ChairpersonBallotRecycleViewLayout);
        grade10ChairpersonRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        grade10ChairpersonRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("G10 Chairperson");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotGrade10PartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotGrade10Profile.getContext()).load(model.getImgID()).into(holder.ballotGrade10Profile);
                holder.ballotGrade10Name.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.grade10RadioBtn.setChecked(true);
                } else {
                    holder.grade10RadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.grade10RadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    grade10Name = holder.ballotGrade10Name.getText().toString();
                    Toast.makeText(getApplicationContext(),grade10Name, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_grade_10_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        grade10ChairpersonRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showSecondYearRepresentativeBallot() {
        RecyclerView secondYearRecyclerView = findViewById(R.id.secondYearRepresentativeBallotRecycleViewLayout);
        secondYearRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        secondYearRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("2nd Year Representative");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotSecondYearRepresentativePartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotSecondYearRepresentativeProfile.getContext()).load(model.getImgID()).into(holder.ballotSecondYearRepresentativeProfile);
                holder.ballotSecondYearRepresentativeName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.secondYearRadioBtn.setChecked(true);
                } else {
                    holder.secondYearRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.secondYearRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    secondYearRepName = holder.ballotSecondYearRepresentativeName.getText().toString();
                    Toast.makeText(getApplicationContext(),secondYearRepName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_second_year_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        secondYearRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showFourthYearRepresentativeBallot() {
        RecyclerView fourthYearRecyclerView = findViewById(R.id.fourthYearRepresentativeBallotRecycleViewLayout);
        fourthYearRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        fourthYearRecyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + LoginVotersActivity.admin_name + " ELECTION")
                .child("Admin " + LoginVotersActivity.admin_name + " CANDIDATES").child("4th Year Representative");
        options = new FirebaseRecyclerOptions.Builder<CandidatesData>().setQuery(reference, CandidatesData.class).build();
        adapter = new FirebaseRecyclerAdapter<CandidatesData, CandidatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder holder, int position, @NonNull CandidatesData model) {
                holder.ballotFourthYearRepresentativePartyList.setText(model.getCandidatesPartyList());
                Glide.with(holder.ballotFourthYearRepresentativeProfile.getContext()).load(model.getImgID()).into(holder.ballotFourthYearRepresentativeProfile);
                holder.ballotFourthYearRepresentativeName.setText(model.getCandidatesName());

                if (sparseBooleanArray.get(position, false)) {
                    holder.fourthYearRadioBtn.setChecked(true);
                } else {
                    holder.fourthYearRadioBtn.setChecked(false);
                }
                setClickListener(holder, position);

            }

            private void setClickListener(final CandidatesViewHolder holder, final int position) {
                holder.fourthYearRadioBtn.setOnClickListener(v -> {
                    sparseBooleanArray.clear();
                    sparseBooleanArray.put(position, true);
                    fourthYearRepName = holder.ballotFourthYearRepresentativeName.getText().toString();
                    Toast.makeText(getApplicationContext(), fourthYearRepName, Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                });
            }

            @NonNull
            @Override
            public CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ballot_fourth_year_recycleviewdata, parent, false);
                return new CandidatesViewHolder(view);
            }
        };
        fourthYearRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirm Exit");
        alertDialog.setMessage("Are you sure you want to exit?");
        alertDialog.setPositiveButton("yes", (dialog, which) -> finish());
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = alertDialog.create();
        alertDialog.show();
        alert.setCanceledOnTouchOutside(false);
    }


}
