package com.project.votingapp.Activities.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.project.votingapp.Activities.AdminActivities.LoginAdminActivity;
import com.project.votingapp.Activities.VotersActivities.LoginVotersActivity;
import com.project.votingapp.Activities.VotersActivities.VotersHomeActivity;
import com.project.votingapp.ConnectionManager.ConnectionManager;
import com.project.votingapp.R;

import java.util.Timer;

public class UserTypeActivity extends AppCompatActivity {

    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_type);

        ConstraintLayout layout = findViewById(R.id.layout);
        ImageView imageLogo = findViewById(R.id.img);
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);
        TextView text5 = findViewById(R.id.text5);
        TextView text6 = findViewById(R.id.text6);
        Button btnTryAgain = findViewById(R.id.btnTryAgain);
        Button btnAdmin = findViewById(R.id.btn_admin);
        Button btnUser = findViewById(R.id.btn_voter);
        ImageView illustration = findViewById(R.id.illustration);

        //Checking Internet Connection
        if (ConnectionManager.isNetworkConnectionAvailable(getBaseContext())){
            //If connected to the internet we disable relative layout etc.
            //and enabling try again button
            layout.setVisibility(View.INVISIBLE);
            imageLogo.setVisibility(View.INVISIBLE);
            text1.setVisibility(View.INVISIBLE);
            text2.setVisibility(View.INVISIBLE);
            text3.setVisibility(View.INVISIBLE);
            btnTryAgain.setVisibility(View.INVISIBLE);

            btnAdmin.setVisibility(View.VISIBLE);
            btnUser.setVisibility(View.VISIBLE);
            text5.setVisibility(View.VISIBLE);
            text6.setVisibility(View.VISIBLE);
            illustration.setVisibility(View.VISIBLE);

            btnAdmin.setOnClickListener(v -> openRegisterAdmin());
            btnUser.setOnClickListener(v -> openRegisterVoter());

        }else{
            //if not connected to the internet we enable relative layout etc.
            //and enabling try again button
            layout.setVisibility(View.VISIBLE);
            imageLogo.setVisibility(View.VISIBLE);
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
            text3.setVisibility(View.VISIBLE);
            btnTryAgain.setVisibility(View.VISIBLE);

            btnAdmin.setVisibility(View.INVISIBLE);
            btnUser.setVisibility(View.INVISIBLE);
            text5.setVisibility(View.INVISIBLE);
            text6.setVisibility(View.INVISIBLE);
            illustration.setVisibility(View.INVISIBLE);

            btnTryAgain.setOnClickListener(v -> {
                //Checking Internet Connection
                if (ConnectionManager.isNetworkConnectionAvailable(getBaseContext())){
                    layout.setVisibility(View.INVISIBLE);
                    imageLogo.setVisibility(View.INVISIBLE);
                    text1.setVisibility(View.INVISIBLE);
                    text2.setVisibility(View.INVISIBLE);
                    text3.setVisibility(View.INVISIBLE);
                    btnTryAgain.setVisibility(View.INVISIBLE);

                    btnAdmin.setVisibility(View.VISIBLE);
                    btnUser.setVisibility(View.VISIBLE);
                    text5.setVisibility(View.VISIBLE);
                    text6.setVisibility(View.VISIBLE);
                    illustration.setVisibility(View.VISIBLE);

                    btnAdmin.setOnClickListener(v1 -> openRegisterAdmin());
                    btnUser.setOnClickListener(v12 -> openRegisterVoter());
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("No Internet Connection");
                    alertDialog.setMessage("Please check your internet settings.");
                    alertDialog.setPositiveButton("ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog alert = alertDialog.create();
                    alertDialog.show();
                    alert.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    public void openRegisterAdmin(){
        startActivity(new Intent(getApplicationContext(), LoginAdminActivity.class));
        finish();
    }

    public void openRegisterVoter(){
        startActivity(new Intent(getApplicationContext(), LoginVotersActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

}
