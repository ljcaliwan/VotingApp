package com.project.votingapp.Activities.VotersActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.R;

import java.util.Objects;

public class LoginVotersActivity extends AppCompatActivity {

    public static SharedPreferences voterPreference;
    private Button btnLoginVoter;
    private FirebaseAuth mAuth;
    private Button addCollegeVoter;
    private TextInputLayout voterID, voterElectionCode, adminName;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    public static String admin_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_voters);

        //Checking if application is opened for the first time
        voterPreference = getSharedPreferences("PREFERENCE_VOTER", MODE_PRIVATE);
        SharedPreferences.Editor editor = voterPreference.edit();
        editor.putString("FirstTimeOpenByVoter", "Yes");
        editor.apply();

        voterID = findViewById(R.id.voterID);
        adminName = findViewById(R.id.adminName);
        voterElectionCode = findViewById(R.id.voterElectionCode);
        btnLoginVoter = findViewById(R.id.btnVoterSubmit);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        loginVoter();

    }

    private void loginVoter() {

        btnLoginVoter.setOnClickListener(v -> {

            String voter_id = Objects.requireNonNull(voterID.getEditText()).getText().toString();
            admin_name = Objects.requireNonNull(adminName.getEditText()).getText().toString();
            String election_code = Objects.requireNonNull(voterElectionCode.getEditText()).getText().toString();

            if (voter_id.isEmpty()) {
                voterID.setError("Please enter voter ID");
            } else if (admin_name.isEmpty()) {
                adminName.setError("Please enter Admin Name");
            } else if (election_code.isEmpty()) {
                voterElectionCode.setError("Please enter Election Code");
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                                                     .child("Admin " + admin_name + " ELECTION")
                                                     .child("Admin " + admin_name + " VOTERS");
                reference.orderByChild("voterID").equalTo(voter_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                                                                 .child("Admin " + admin_name + " ELECTION");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String electionCode = Objects.requireNonNull(snapshot.child("electionCode").getValue()).toString();
                                        if (electionCode.equalsIgnoreCase(election_code)) {
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), VotersHomeActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Incorrect election code", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Election does not exist", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect voter ID", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        VotersHomeActivity.voterHomePreference = getSharedPreferences("PREFERENCE_VOTER_HOME", MODE_PRIVATE);
//        String secondTimeOpenVoterHome = VotersHomeActivity.voterHomePreference.getString("FirstTimeOpenVoterHome", "");
//
//        if (secondTimeOpenVoterHome.equals("Yes")) {
//            startActivity(new Intent(getApplicationContext(), VotersHomeActivity.class));
//            finish();
//        }
//
//    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
