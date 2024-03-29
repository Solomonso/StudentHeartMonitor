package com.example.studentheartmonitor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.studentheartmonitor.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.studentheartmonitor.R;
import com.example.studentheartmonitor.helper.SQLiteHandler;
import com.example.studentheartmonitor.helper.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.example.studentheartmonitor.*;

public class LessonOverview extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView textLessonName;
    private TextView textStudentName;
    private TextView textStudentBpm;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_overview);

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
        drawerLayout = findViewById(R.id.drawer_layout_overview);
        navigationView = findViewById(R.id.navigationView);

        //get the navigation items selected on the menu toolbar
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_lesson_home:
                        menuItem.setChecked(true);
                        openTeacherHomePage();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        //store date details
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //to display the date
        TextView textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);

        //get the lesson_overview_name  by id
        textLessonName = findViewById(R.id.lesson_overview_name);

        //get the student name and student bpm
        textStudentName = findViewById(R.id.student_name);
        textStudentBpm = findViewById(R.id.student_bpm);

        // Fetching lesson details from SQLite
        HashMap<String,String> lesson = db.getUserLessonDetails();

        //Fetching heart rate details From SQLite
        HashMap<String,String> heart = db.getHeartDetails();

        //Fetching student details From SQLite
        HashMap<String,String> student = db.getStudentDetailsForTeacher();

        IsCheckIn checkIn = new IsCheckIn();
        if(checkIn.getCheckIn())
        {

            String name = student.get("student_username");
            String nameInfo = "";
            if(name != null)
            {
                nameInfo = nameInfo + " " + name;
                textStudentName.setText(nameInfo);
            }
            else
            {
                nameInfo = nameInfo + "Student Name Here ";
                textStudentName.setText(nameInfo);
            }

            //displaying the student name
            //textStudentName.setText(name);


            String heartRate = heart.get("heartrate");
            String hearRateInfo = "";
            if(heartRate == null || heartRate.equalsIgnoreCase("Remove and place finger") || heartRate.equalsIgnoreCase("momove and place finger") || heartRate.equalsIgnoreCase("eemove and place finger") || heartRate.equalsIgnoreCase("oveove and place finger") || heartRate.equalsIgnoreCase("No connectionsNo connectionsNo connectionsNo connectionsNo connections 50") || heartRate.equalsIgnoreCase("No connectionsRemove and place finger"))
            {

                hearRateInfo = hearRateInfo + "BPM ";
                textStudentBpm.setText(hearRateInfo);
            }
            else
            {
                hearRateInfo = hearRateInfo + "BPM is " + heartRate;
                textStudentBpm.setText(hearRateInfo);
            }
        }

        if(!checkIn.getCheckIn())
        {

            String name = student.get("student_username");
            String nameInfo = "";
            if(name != null)
            {
                nameInfo = nameInfo + " " + name;
                textStudentName.setText(nameInfo);
            }
            else
            {
                nameInfo = nameInfo + "Student Name Here ";
                textStudentName.setText(nameInfo);
            }

            //displaying the student name
            //textStudentName.setText(name);


            String heartRate = heart.get("heartrate");
            String hearRateInfo = "";
            if(heartRate == null || heartRate.equalsIgnoreCase("Remove and place finger") || heartRate.equalsIgnoreCase("momove and place finger") || heartRate.equalsIgnoreCase("eemove and place finger") || heartRate.equalsIgnoreCase("oveove and place finger"))
            {

                hearRateInfo = hearRateInfo + "BPM ";
                textStudentBpm.setText(hearRateInfo);
            }
            else
            {
                hearRateInfo = hearRateInfo + "BPM is " + heartRate;
                textStudentBpm.setText(hearRateInfo);
            }
        }

        //get lesson code
        String lessonName = lesson.get("lesson_name");
        String info = "";
        if(lessonName != null)
        {
            info = info + "Lesson \n" + lessonName;
            textLessonName.setText(info);
        }
        else
        {
            info = info + "Lesson \n Not Set";
            textLessonName.setText(info);
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

    public void openTeacherHomePage()
    {
        Intent intent = new Intent(LessonOverview.this,com.example.studentheartmonitor.activity.TeacherActivity.class);
        startActivity(intent);
    }
}
