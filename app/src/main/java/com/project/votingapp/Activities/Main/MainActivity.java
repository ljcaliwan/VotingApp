package com.project.votingapp.Activities.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.project.votingapp.Activities.AdminActivities.LoginAdminActivity;
import com.project.votingapp.Activities.VotersActivities.LoginVotersActivity;
import com.project.votingapp.Activities.VotersActivities.VotersHomeActivity;
import com.project.votingapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Checking if application is opened for the first time
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String firstTime = preferences.getString("FirstTimeInstall", "");

        LoginVotersActivity.voterPreference = getSharedPreferences("PREFERENCE_VOTER", MODE_PRIVATE);
        String firstTimeOpenByVoter = LoginVotersActivity.voterPreference.getString("FirstTimeOpenByVoter", "");

        LoginAdminActivity.adminPreference = getSharedPreferences("PREFERENCE_ADMIN", MODE_PRIVATE);
        String firstTimeOpenByAdmin = LoginAdminActivity.adminPreference.getString("FirstTimeOpenByAdmin", "");

        if (firstTime.equals("Yes")){//if application was opened for the second time, execution goes here

            if (firstTimeOpenByVoter.equals("Yes")){
                startActivity(new Intent(getApplicationContext(), LoginVotersActivity.class));
                finish();
            }else if (firstTimeOpenByAdmin.equals("Yes")){
                startActivity(new Intent(getApplicationContext(), LoginAdminActivity.class));
                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), UserTypeActivity.class));
                finish();
            }
        }else{
            //Else... if application was opened for the first time
            //Splash Screen
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), UserTypeActivity.class));
                finish();
            }, 4000); //4s

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall", "Yes");
            editor.apply();

        }
    }
}
