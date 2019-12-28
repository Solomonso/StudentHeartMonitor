package com.example.studentheartmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.studentheartmonitor.activity.LoginTeacherPage;


public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button buttonStudent;
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(MainActivity.this, "firebase connection success",Toast.LENGTH_LONG).show();
        buttonStudent = findViewById(R.id.buttonStudent);
        buttonStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openStudentLoginPage();
            }
        });

        //teacher buttons
        button = findViewById(R.id.buttonTeacher);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openTeacherLoginPage();
                //added comment
            }
        });
    }
    //student
    public void openStudentLoginPage()
    {
        Intent intent = new Intent(MainActivity.this,com.example.studentheartmonitor.activity.LoginStudentPage.class);
        startActivity(intent);
    }

    //teacher
    public void openTeacherLoginPage()
    {
        Intent intent = new Intent(MainActivity.this,com.example.studentheartmonitor.activity.LoginTeacherPage.class);
        startActivity(intent);
    }
}
