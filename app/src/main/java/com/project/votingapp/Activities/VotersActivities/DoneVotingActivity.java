package com.project.votingapp.Activities.VotersActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.project.votingapp.R;

public class DoneVotingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_voting);

        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginVotersActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
