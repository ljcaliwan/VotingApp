package com.project.votingapp.Activities.AdminActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.votingapp.Activities.Model.ElectionData;
import com.project.votingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateElectionActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spinner;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    public String electionType;
    public static boolean isCreated, isCollegeElection, isHSElection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        spinner = findViewById(R.id.electionType);
        electionTypeSpinner();

        ImageView imageView = findViewById(R.id.imageView);
        EditText electionName = findViewById(R.id.electionName);
        EditText electionCode = findViewById(R.id.electionCode);
        TextView navUserEmail = findViewById(R.id.nav_user_mail);
        Button btnCreateElection = findViewById(R.id.btn_createElection);

        ElectionData dataManager = new ElectionData();
        reference = FirebaseDatabase.getInstance().getReference().child("Election List");
        reference.orderByChild("adminEmail").equalTo(currentUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //auto increment ID
                    //maxID = (int) dataSnapshot.getChildrenCount();
                    // String electionName = Objects.requireNonNull(dataSnapshot.child("electionName").getValue()).toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnCreateElection.setOnClickListener(v -> {
            //electionData = new ArrayList();
            String election_name = electionName.getText().toString();
            String election_code = electionCode.getText().toString();

            if (election_name.isEmpty() && election_code.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
            } else if (election_name.isEmpty()) {
                electionName.setError("Election name cannot be empty");
            } else if (election_code.isEmpty()) {
                electionCode.setError("Election code cannot be empty");
            } else if (electionType.equalsIgnoreCase("Select election type")) {
                Toast.makeText(getApplicationContext(), "Please select election type", Toast.LENGTH_LONG).show();
            } else {
                dataManager.setElectionName(election_name);
                dataManager.setElectionCode(election_code);
                dataManager.setElectionType(electionType);
                dataManager.setAdminEmail(currentUser.getEmail());
                reference.child("Admin " + currentUser.getDisplayName() + " ELECTION").setValue(dataManager);
                Toast.makeText(CreateElectionActivity.this, "Election Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminHome.class));
                finish();
                isCreated = true;

            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Back arrow on click listener
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            isCreated = false;
        });
    }

    public void electionTypeSpinner() {

        String[] election_type = new String[]{"Select election type",
                "Junior/Senior High Election (SSG ELECTION)",
                "College Election (SSC ELECTION)"};

        List<String> electionTypeList = new ArrayList<>(Arrays.asList(election_type));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, electionTypeList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                electionType = parent.getItemAtPosition(position).toString();
                if (position > 0) {
                    if (position == 1) {
                        isHSElection = true;
                        isCollegeElection = false;
                    } else {
                        isCollegeElection = true;
                        isHSElection = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        isCreated = false;
    }

}
