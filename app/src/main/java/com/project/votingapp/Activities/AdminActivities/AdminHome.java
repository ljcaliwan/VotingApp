package com.project.votingapp.Activities.AdminActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.votingapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminHome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.nav_signOut).setOnMenuItemClickListener(menuItem -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Confirm Logout");
            alertDialog.setMessage("Are you sure you want to logout?");
            alertDialog.setPositiveButton("yes", (dialog, which) -> logOutAdmin());
            alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog alert = alertDialog.create();
            alertDialog.show();
            alert.setCanceledOnTouchOutside(false);
            return true;

        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_tallyBoard, R.id.nav_voters,
                R.id.nav_candidates, R.id.nav_electionSetting, R.id.nav_position)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavHeaderInfo();

    }

    public void updateNavHeaderInfo(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_mail);
        CircleImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        //updating nav header info
        navUsername.setText(currentUser.getDisplayName());
        navUserEmail.setText(currentUser.getEmail());

        //loading user image from firebase using Glide library
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logOutAdmin() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginAdminActivity.class));
        finish();
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
