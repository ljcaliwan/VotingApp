package com.project.votingapp.Activities.AdminActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.votingapp.R;

import java.util.Objects;

public class LoginAdminActivity extends AppCompatActivity {
    private Button btnSignIn;
    private TextInputLayout userEmail, userPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    public static SharedPreferences adminPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_admin);

        adminPreference = getSharedPreferences("PREFERENCE_ADMIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = adminPreference.edit();
        editor.putString("FirstTimeOpenByAdmin", "Yes");
        editor.apply();

        userEmail = findViewById(R.id.txtEmail);
        userPassword = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(v -> {

            progressDialog = new ProgressDialog(LoginAdminActivity.this);

            String mail =  Objects.requireNonNull(userEmail.getEditText()).getText().toString();
            String password = Objects.requireNonNull(userPassword.getEditText()).getText().toString();
            if(mail.isEmpty() && password.isEmpty()){
                showMessage("Email and Password are required!");
                btnSignIn.setVisibility(View.VISIBLE);
            }else if (mail.isEmpty()){
                userEmail.setError("Email cannot be empty");
                btnSignIn.setVisibility(View.VISIBLE);
            }else if(password.isEmpty()){
                userPassword.setError("Password cannot be empty");
                btnSignIn.setVisibility(View.VISIBLE);
            }else{
                signIn(mail, password);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog2);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }
        });

        btnSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpAdminActivity.class)));
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                btnSignIn.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), AdminHome.class));
                finish();
                progressDialog.dismiss();
            }else{
                showMessage(Objects.requireNonNull(task.getException()).getMessage());
                progressDialog.dismiss();
                btnSignIn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showMessage(String message) { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show(); }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        //redirecting admin to home page
        if (user != null){
            startActivity(new Intent(getApplicationContext(), AdminHome.class));
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}



