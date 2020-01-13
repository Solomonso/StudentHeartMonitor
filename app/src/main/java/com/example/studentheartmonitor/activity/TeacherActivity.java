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


import com.example.studentheartmonitor.MainActivity;
import com.example.studentheartmonitor.R;
import com.example.studentheartmonitor.helper.SQLiteHandler;
import com.example.studentheartmonitor.helper.SessionManager;
import com.google.android.material.navigation.NavigationView;

import static java.nio.file.Paths.get;


public class TeacherActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtName;
    private TextView txtEmail;
    private Button buttonCreateLesson;
    private Button buttonLessonOverview;
    private TextView textLessonCode;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

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

        //create lesson button
        buttonCreateLesson = findViewById(R.id.createLesson);
        buttonCreateLesson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openCreateLesson();
            }
        });

        //lesson overview button
        buttonLessonOverview = findViewById(R.id.lessonOverview);
        buttonLessonOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openLessonOverview();
            }
        });
        //menus in the navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        //get the navigation items selected on the menu toolbar
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_lesson_overview:
                        menuItem.setChecked(true);
                        openLessonOverview();
                        drawerLayout.closeDrawers();
                        return true;

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

        //get the lesson_code by id
        textLessonCode = findViewById(R.id.text_view_lesson_code);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        // Fetching lesson details from SQLite
        HashMap<String,String> lesson = db.getUserLessonDetails();

        String name = user.get("teacher_username");
        String email = user.get("teacher_email");

        //get lesson code
        String lessonCode = lesson.get("lesson_code");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        //displaying the lesson code on the screen
        String info = "";
        if(lessonCode != null) {
            info = info + "Lesson Code is " + lessonCode;
            textLessonCode.setText(info);
        }
        else
        {
            info = info + "Lesson Code Appear Here ";
            textLessonCode.setText(info);
        }

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
        db.deleteLessons();
        db.deleteUsers();

        session.logOut();
    }

    //teacher lesson overview
    public void openLessonOverview()
    {
        Intent intent = new Intent(TeacherActivity.this,com.example.studentheartmonitor.activity.LessonOverview.class);
        startActivity(intent);
    }

    //teacher create lesson
    public void openCreateLesson()
    {
        Intent intent = new Intent(TeacherActivity.this,com.example.studentheartmonitor.activity.CreateLessonActivity.class);
        startActivity(intent);
    }
}
