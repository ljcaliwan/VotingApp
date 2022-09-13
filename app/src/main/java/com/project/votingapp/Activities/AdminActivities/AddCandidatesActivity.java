package com.project.votingapp.Activities.AdminActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.votingapp.Activities.Model.CandidatesData;
import com.project.votingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCandidatesActivity extends AppCompatActivity {

    long maxID = 0;
    Toolbar toolbar;
    String election_type;
    int votes = 0;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ValueEventListener listener;
    private ProgressDialog progressDialog;
    CandidatesData candidatesData;
    DatabaseReference reference, ref;
    int PreCode = 1;
    private int REQUEST_CODE = 1;
    private CircleImageView candidatePhoto;
    Uri pickedImgUri;
    ConstraintLayout highSchoolLayout, collegeLayout;
    String h_positionRunningFor, c_positionRunningFor, h_partyList, c_partyList;
    private EditText highSchoolCandidatesID, highSchoolCandidatesName;
    private EditText highSchoolCandidatesGradeLevel, highSchoolCandidatesSection;
    private EditText collegeCandidatesID, collegeCandidatesName;
    private EditText collegeCandidatesYearLevel, collegeCandidatesSection;
    private Spinner highSchoolCandidatesPosition, collegeCandidatesPosition, h_partyListSpinner, c_partyListSpinner;
    private Button addHighSchoolCandidates, addCollegeCandidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidates);

        toolbar = findViewById(R.id.toolbar_Candidates);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Back arrow on click listener
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        addHighSchoolCandidates = findViewById(R.id.h_addCandidates);
        addCollegeCandidates = findViewById(R.id.c_addCandidates);

        highSchoolLayout = findViewById(R.id.h_layout);
        collegeLayout = findViewById(R.id.c_layout);

        highSchoolCandidatesID = findViewById(R.id.h_LRN);
        highSchoolCandidatesName = findViewById(R.id.h_fullName);
        highSchoolCandidatesGradeLevel = findViewById(R.id.h_gradeLevel);
        highSchoolCandidatesSection = findViewById(R.id.h_section);
        highSchoolCandidatesPosition = findViewById(R.id.h_positionRunningForSpinner);
        h_partyListSpinner = findViewById(R.id.h_partyListSpinner);

        collegeCandidatesID = findViewById(R.id.c_LRN);
        collegeCandidatesName = findViewById(R.id.c_fullName);
        collegeCandidatesYearLevel = findViewById(R.id.c_yearLevel);
        collegeCandidatesSection = findViewById(R.id.c_section);
        collegeCandidatesPosition = findViewById(R.id.c_positionRunningForSpinner);
        c_partyListSpinner = findViewById(R.id.c_partyListSpinner);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (!CreateElectionActivity.isCreated) {
            collegeLayout.setVisibility(View.INVISIBLE);
            highSchoolLayout.setVisibility(View.INVISIBLE);
        }

        if (CreateElectionActivity.isCreated && CreateElectionActivity.isHSElection) {
            //Show HS election layout
            collegeLayout.setVisibility(View.INVISIBLE);
            highSchoolLayout.setVisibility(View.VISIBLE);
        } else {
            //Show college election layout
            collegeLayout.setVisibility(View.VISIBLE);
            highSchoolLayout.setVisibility(View.INVISIBLE);
        }


        candidatePhoto = findViewById(R.id.addImgCandidate);
        candidatePhoto.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 22) {
                checkAndRequestForPermission();
            } else {
                openGallery();
            }
        });

        highSchoolPartyListSpinner();
        highSchoolPositionRunningForSpinner();
        collegePartyListSpinner();
        collegePositionRunningForSpinner();
        checkElectionType();

    }

    public void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(AddCandidatesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddCandidatesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(AddCandidatesActivity.this, "Please accept for permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(AddCandidatesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PreCode);
            }
        } else {
            openGallery();
        }

    }

    public void openGallery() {
        // Open gallery intent and wait user to pick image
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            //the user has successfully picked an image
            //save its reference to a Uri variable
            pickedImgUri = data.getData();
            candidatePhoto.setImageURI(pickedImgUri);

        }
    }

    private void addCandidates() {

        progressDialog = new ProgressDialog(AddCandidatesActivity.this);

        addHighSchoolCandidates.setOnClickListener(v -> {

            String high_school_candidates_id = Objects.requireNonNull(highSchoolCandidatesID.getText().toString());
            String high_school_candidates_name = Objects.requireNonNull(highSchoolCandidatesName.getText().toString());
            String high_school_candidates_grade_level = Objects.requireNonNull(highSchoolCandidatesGradeLevel.getText().toString());
            String high_school_candidates_section = Objects.requireNonNull(highSchoolCandidatesSection.getText().toString());

            if (high_school_candidates_id.isEmpty()) {
                highSchoolCandidatesID.setError("Please enter Candidates LRN");
            } else if (high_school_candidates_name.isEmpty()) {
                highSchoolCandidatesName.setError("Please enter Candidates full name");
            } else if (h_partyList.equalsIgnoreCase("Select Partylist")) {
                Toast.makeText(getApplicationContext(), "Please select candidates partylist", Toast.LENGTH_LONG).show();
            } else if (high_school_candidates_grade_level.isEmpty()) {
                highSchoolCandidatesGradeLevel.setError("Please enter Candidates grade level");
            } else if (high_school_candidates_section.isEmpty()) {
                highSchoolCandidatesSection.setError("Please enter Candidates section");
            } else if (h_positionRunningFor.equalsIgnoreCase("Select Position Running For")) {
                Toast.makeText(getApplicationContext(), "Please select candidates position", Toast.LENGTH_LONG).show();
            } else if (pickedImgUri == null) {
                Toast.makeText(getApplicationContext(), "Please select candidates photo", Toast.LENGTH_LONG).show();
            } else {
                if (h_positionRunningFor.equalsIgnoreCase("President")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("President");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("Vice President")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Vice President");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else if (h_positionRunningFor.equalsIgnoreCase("Secretary")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Secretary");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("Auditor")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Auditor");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("Treasurer")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Treasurer");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("Public Information Officer")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Public Information Officer");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("Peace Officer")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Peace Officer");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("G9 Chairperson")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("G9 Chairperson");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (h_positionRunningFor.equalsIgnoreCase("G10 Chairperson")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("high_school_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imgUrl = uri.toString();
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                    ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("G10 Chairperson");
                                    candidatesData = new CandidatesData(imgUrl, high_school_candidates_id, high_school_candidates_name,
                                            h_partyList, high_school_candidates_grade_level,
                                            high_school_candidates_section, h_positionRunningFor, votes);
                                    ref.child(high_school_candidates_name).setValue(candidatesData);
                                    Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select candidates position", Toast.LENGTH_SHORT).show();
                }

            }

        });

        addCollegeCandidates.setOnClickListener(v -> {

            progressDialog = new ProgressDialog(AddCandidatesActivity.this);

            String college_candidates_ID = Objects.requireNonNull(collegeCandidatesID.getText().toString());
            String college_candidates_name = Objects.requireNonNull(collegeCandidatesName.getText().toString());
            String college_candidates_year_level = Objects.requireNonNull(collegeCandidatesYearLevel.getText().toString());
            String college_candidates_section = Objects.requireNonNull(collegeCandidatesSection.getText().toString());
            //String imageID = System.currentTimeMillis() + "." + getExtension(pickedImgUri);

            if (college_candidates_ID.isEmpty()) {
                collegeCandidatesID.setError("Please enter Candidates student ID");
            } else if (college_candidates_name.isEmpty()) {
                collegeCandidatesName.setError("Please enter Candidates full name");
            } else if (c_partyList.equalsIgnoreCase("Select Partylist")) {
                Toast.makeText(getApplicationContext(), "Please select candidates partylist", Toast.LENGTH_LONG).show();
            } else if (college_candidates_year_level.isEmpty()) {
                collegeCandidatesYearLevel.setError("Please enter Candidates year level");
            } else if (college_candidates_section.isEmpty()) {
                collegeCandidatesSection.setError("Please enter Candidates section");
            } else if (c_positionRunningFor.equalsIgnoreCase("Select Position Running For")) {
                Toast.makeText(getApplicationContext(), "Please select candidates position", Toast.LENGTH_LONG).show();
            } else if (pickedImgUri == null) {
                Toast.makeText(getApplicationContext(), "Please select candidates photo", Toast.LENGTH_LONG).show();
            } else {
                if (c_positionRunningFor.equalsIgnoreCase("President")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);
                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("President");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (c_positionRunningFor.equalsIgnoreCase("Vice President")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);
                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Vice President");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else if (c_positionRunningFor.equalsIgnoreCase("Secretary")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Secretary");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (c_positionRunningFor.equalsIgnoreCase("Auditor")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Auditor");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (c_positionRunningFor.equalsIgnoreCase("Treasurer")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Treasurer");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (c_positionRunningFor.equalsIgnoreCase("Public Information Officer")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Public Information Officer");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (c_positionRunningFor.equalsIgnoreCase("2nd Year Representative")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("2nd Year Representative");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (c_positionRunningFor.equalsIgnoreCase("4th Year Representative")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                            .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                            .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("All Candidates");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //auto increment ID
                                maxID = (int) dataSnapshot.getChildrenCount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    try {
                        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("college_candidates_photos");
                        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successful", Toast.LENGTH_SHORT).show();
                            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String imgUrl = uri.toString();
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                reference.child(String.valueOf(maxID + 1)).setValue(candidatesData);

                                ref = FirebaseDatabase.getInstance().getReference().child("Election List")
                                        .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                                        .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("4th Year Representative");
                                candidatesData = new CandidatesData(imgUrl, college_candidates_ID, college_candidates_name,
                                        c_partyList, college_candidates_year_level,
                                        college_candidates_section, c_positionRunningFor, votes);
                                ref.child(college_candidates_name).setValue(candidatesData);
                                Toast.makeText(getApplicationContext(), "Candidates added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }).addOnProgressListener(taskSnapshot -> {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog3);
                            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show());

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select candidates position", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void checkElectionType() {
        reference = FirebaseDatabase.getInstance().getReference().child("Election List").child("Admin " + currentUser.getDisplayName() + " ELECTION");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    election_type = Objects.requireNonNull(snapshot.child("electionType").getValue()).toString();
                    if (election_type.equalsIgnoreCase("College Election (SSC ELECTION)")) {
                        collegeLayout.setVisibility(View.VISIBLE);
                        highSchoolLayout.setVisibility(View.INVISIBLE);
                        addCandidates();
                    } else {
                        collegeLayout.setVisibility(View.INVISIBLE);
                        highSchoolLayout.setVisibility(View.VISIBLE);
                        addCandidates();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Election not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void highSchoolPartyListSpinner() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Partylist");

        String[] partyList = new String[]{"Select Partylist"};

        List<String> spinnerData = new ArrayList<>(Arrays.asList(partyList));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerData) {
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
        h_partyListSpinner.setAdapter(arrayAdapter);
        h_partyListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                h_partyList = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    spinnerData.add(Objects.requireNonNull(item.getValue()).toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void collegePartyListSpinner() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Partylist");

        String[] partyList = new String[]{"Select partylist"};

        List<String> spinnerData = new ArrayList<>(Arrays.asList(partyList));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerData) {
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
        c_partyListSpinner.setAdapter(arrayAdapter);
        c_partyListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c_partyList = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    spinnerData.add(Objects.requireNonNull(item.getValue()).toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void highSchoolPositionRunningForSpinner() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Position");

        String[] position = new String[]{"Select Position Running For"};

        List<String> spinnerData = new ArrayList<>(Arrays.asList(position));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerData) {
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
        highSchoolCandidatesPosition.setAdapter(arrayAdapter);
        highSchoolCandidatesPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                h_positionRunningFor = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    spinnerData.add(Objects.requireNonNull(item.getValue()).toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void collegePositionRunningForSpinner() {

        reference = FirebaseDatabase.getInstance().getReference().child("Election List")
                .child("Admin " + currentUser.getDisplayName() + " ELECTION")
                .child("Admin " + currentUser.getDisplayName() + " CANDIDATES").child("Candidates Position");

        String[] position = new String[]{"Select Position Running For"};

        List<String> spinnerData = new ArrayList<>(Arrays.asList(position));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerData) {
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
        collegeCandidatesPosition.setAdapter(arrayAdapter);
        collegeCandidatesPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c_positionRunningFor = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    spinnerData.add(Objects.requireNonNull(item.getValue()).toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
