package com.example.studentheartmonitor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.time.format.DateTimeFormatter;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;


import com.example.studentheartmonitor.MainActivity;
import com.example.studentheartmonitor.R;
import com.example.studentheartmonitor.helper.SQLiteHandler;
import com.example.studentheartmonitor.helper.SessionManager;
import com.google.android.material.navigation.NavigationView;


public class StudentActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtName;
    private TextView txtEmail;
    private TextView BPM;
    private TextView avgBPM;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //adding the action bar
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        //menus in the navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        //get the navigation items selected on the menu toolbar
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
//                    case R.id.nav_join_lesson:
//                        menuItem.setChecked(true);
//                        displayMessage("Join lesson selected...");
//                        drawerLayout.closeDrawers();
//                        return true;

                    case R.id.nav_log_out:
                        menuItem.setChecked(true);
                        logoutUser();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
        //store date details
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //to display the date
        TextView textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);

        //get the name and email by id
        txtName = findViewById(R.id.name);
        txtEmail =  findViewById(R.id.email);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        showBPM();
        showAvgBPM();
    }

    List<Integer> heartBeat = new ArrayList<>();
    public void showBPM ()
    {
        int currentBPM = 0;
        heartBeat.add(currentBPM);

        BPM = findViewById(R.id.BPM);
        BPM.setText(currentBPM);
    }

    public void showAvgBPM ()
    {
        int sum = 0;
        int average;
        for (int beat : this.heartBeat)
        {
            sum = sum + beat;
        }
        average = sum / heartBeat.size();
        avgBPM = findViewById(R.id.AvgBPM);
        avgBPM.setText(average);
    }


    //method to make the action bar clickable
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //for testing. to display the current item clicked on the menu bar
    private void displayMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(StudentActivity.this, LoginStudentPage.class);
        startActivity(intent);
        finish();
    }
}
