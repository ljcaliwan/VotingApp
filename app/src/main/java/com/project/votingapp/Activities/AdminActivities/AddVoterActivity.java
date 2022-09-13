package com.project.votingapp.Activities.AdminActivities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.Activities.Main.UserTypeActivity;
import com.project.votingapp.Activities.Model.VotersData;
import com.project.votingapp.R;

import java.util.Objects;

public class AddVoterActivity extends AppCompatActivity {

    long maxID = 0;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    Button addCollegeVoter;
    VotersData votersData;
    FirebaseUser currentUser;
    DatabaseReference reference;
    ConstraintLayout collegeLayout;
    Button addHighSchoolVoter;
    private EditText highSchoolVoterName, highSchoolVoterLRN, highSchoolVoterGradeLvl, highSchoolVoterSection;
    private EditText collegeVoterName, collegeVoterStudID, collegeVoterYearLvl, collegeVoterSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voter);

        collegeLayout = findViewById(R.id.college_layout);

        highSchoolVoterName = findViewById(R.id.highSchoolVoterName);
        highSchoolVoterLRN = findViewById(R.id.highSchoolVoterLRN);
        highSchoolVoterGradeLvl = findViewById(R.id.highSchoolVoterGradeLvl);
        highSchoolVoterSection = findViewById(R.id.highSchoolVoterSection);
        addHighSchoolVoter = findViewById(R.id.btn_addVoterHighSchool);

        collegeVoterName = findViewById(R.id.voterFullName);
        collegeVoterStudID = findViewById(R.id.voterStudentID);
        collegeVoterYearLvl = findViewById(R.id.voterYearLvl);
        collegeVoterSection = findViewById(R.id.voterSection2);
        addCollegeVoter = findViewById(R.id.btn_addVoterCollege);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        votersData = new VotersData();

        if (CreateElectionActivity.isCreated && CreateElectionActivity.isHSElection ){
            //Show HS election layout
            highSchoolVoterName.setVisibility(View.VISIBLE);
            highSchoolVoterLRN.setVisibility(View.VISIBLE);
            highSchoolVoterGradeLvl.setVisibility(View.VISIBLE);
            highSchoolVoterSection.setVisibility(View.VISIBLE);
            addHighSchoolVoter.setVisibility(View.VISIBLE);
            collegeLayout.setVisibility(View.INVISIBLE);
        }else {
            //Show college election layout
            highSchoolVoterName.setVisibility(View.INVISIBLE);
            highSchoolVoterLRN.setVisibility(View.INVISIBLE);
            highSchoolVoterGradeLvl.setVisibility(View.INVISIBLE);
            highSchoolVoterSection.setVisibility(View.INVISIBLE);
            addHighSchoolVoter.setVisibility(View.INVISIBLE);
            collegeLayout.setVisibility(View.VISIBLE);
        }

        checkElectionType();

        toolbar = findViewById(R.id.toolbar_Voter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Back arrow on click listener
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    public void checkElectionType(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List").child("Admin " + currentUser.getDisplayName() + " ELECTION");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String election_type = Objects.requireNonNull(snapshot.child("electionType").getValue()).toString();
                    if (election_type.equalsIgnoreCase("College Election (SSC ELECTION)")){
                        highSchoolVoterName.setVisibility(View.INVISIBLE);
                        highSchoolVoterLRN.setVisibility(View.INVISIBLE);
                        highSchoolVoterGradeLvl.setVisibility(View.INVISIBLE);
                        highSchoolVoterSection.setVisibility(View.INVISIBLE);
                        addHighSchoolVoter.setVisibility(View.INVISIBLE);
                        collegeLayout.setVisibility(View.VISIBLE);
                        addVoters();
                    }else{
                        highSchoolVoterName.setVisibility(View.VISIBLE);
                        highSchoolVoterLRN.setVisibility(View.VISIBLE);
                        highSchoolVoterGradeLvl.setVisibility(View.VISIBLE);
                        highSchoolVoterSection.setVisibility(View.VISIBLE);
                        addHighSchoolVoter.setVisibility(View.VISIBLE);
                        collegeLayout.setVisibility(View.INVISIBLE);
                        addVoters();
                   }
                }else{
                    Toast.makeText(getApplicationContext(), "Election not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addVoters(){
        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " VOTERS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //auto increment ID
                    maxID = (int) dataSnapshot.getChildrenCount();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addHighSchoolVoter.setOnClickListener(v -> {
            String high_school_voter_name =  highSchoolVoterName.getText().toString();
            String high_school_voter_LRN = highSchoolVoterLRN.getText().toString();
            String high_school_voter_grade_level = highSchoolVoterGradeLvl.getText().toString();
            String high_school_voter_section = highSchoolVoterSection.getText().toString();

            if (high_school_voter_name.isEmpty()){
                highSchoolVoterName.setError("Please enter full name");
            }else if ( high_school_voter_LRN.isEmpty()){
                highSchoolVoterLRN.setError("Please enter LRN");
            }else if ( high_school_voter_grade_level.isEmpty()){
                highSchoolVoterGradeLvl.setError("Please enter grade level");
            }else if (high_school_voter_section.isEmpty()){
                highSchoolVoterSection.setError("Please enter section");
            }else{
                try {
                    votersData.setVoterName(high_school_voter_name);
                    votersData.setVoterID(high_school_voter_LRN);
                    votersData.setVoterYearLevel(high_school_voter_grade_level);
                    votersData.setVoterSection(high_school_voter_section);
                    reference.child(String.valueOf(maxID + 1)).setValue(votersData);
                    Toast.makeText(getApplicationContext(), "Voter added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        addCollegeVoter.setOnClickListener(v -> {
            String college_voter_name =  collegeVoterName.getText().toString();
            String college_voter_ID = collegeVoterStudID.getText().toString();
            String college_voter_year_level = collegeVoterYearLvl.getText().toString();
            String college_voter_section = collegeVoterSection.getText().toString();

            if (college_voter_name.isEmpty()){
                collegeVoterName.setError("Please enter full name");
            }else if ( college_voter_ID.isEmpty()){
                collegeVoterStudID.setError("Please enter LRN");
            }else if ( college_voter_year_level.isEmpty()){
                collegeVoterYearLvl.setError("Please enter grade level");
            }else if (college_voter_section.isEmpty()){
                collegeVoterSection.setError("Please enter section");
            }else{
                try {
                    votersData.setVoterName(college_voter_name);
                    votersData.setVoterID(college_voter_ID);
                    votersData.setVoterYearLevel(college_voter_year_level);
                    votersData.setVoterSection(college_voter_section);
                    reference.child(String.valueOf(maxID + 1)).setValue(votersData);
                    Toast.makeText(getApplicationContext(), "Voter added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
