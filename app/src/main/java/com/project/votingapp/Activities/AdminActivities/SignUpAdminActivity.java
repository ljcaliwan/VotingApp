package com.project.votingapp.Activities.AdminActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.votingapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpAdminActivity extends AppCompatActivity {

    static int PreCode = 1;
    static int REQUESTCODE = 1;
    CircleImageView ImgUserPhoto;
    Uri pickedImgUri;

    private EditText userName, userEmail, userPassword, userPassword2;
    private ProgressBar loadingProgress;
    private Button regBtn;

    private TextView txtSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_admin);

        //initializing here
        txtSignIn = findViewById(R.id.txtSignIn);
        userName = findViewById(R.id.regName);
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        regBtn =findViewById(R.id.regBtn);
        loadingProgress = findViewById(R.id.progressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAdminActivity.class));
                finish();
            }
        });

        //register button
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);

                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || !password.equals(password2)){

                    //if something is empty
                    //we need to display an error
                    showMessage("Please verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }else{

                    //everything is okay, all fields are filled and we can start creating user account
                    createUserAccount(name, email, password);

                }
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22){

                    checkAndRequestForPermission();

                }else{
                    openGallery();
                }

            }
        });
    }
    //create user account with specific email and password
    private void createUserAccount(final String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //user account created successfully!
                    showMessage("Account created");
                    //after we created user account we need to update his/her info
                    updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());

                }else{

                    //account creation failed!
                    showMessage("account creation failed!" + task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    //update user info
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {

        //first we need to upload user photo to firebase storage and get uri
        StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image uploaded successfully
                //we can get now our img uri
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //uri contain user image
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            //user info update successfully
                                            showMessage("Register complete");

                                        }

                                    }
                                });
                    }
                });

            }
        });

    }

    //toast a message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){

            //the user has successfully picked an image
            //we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);

        }
    }

    private void openGallery() {


        // Open gallery intent and wait user to pick image
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);

    }
    //checking for permission
    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(SignUpAdminActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
           != PackageManager.PERMISSION_GRANTED){
               if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpAdminActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                   Toast.makeText(SignUpAdminActivity.this, "Please accept for permission", Toast.LENGTH_SHORT).show();

               }else{
                    ActivityCompat.requestPermissions(SignUpAdminActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PreCode);

               }

        } else {
            openGallery();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
