package com.example.studentheartmonitor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentheartmonitor.R;
import com.example.studentheartmonitor.helper.SQLiteHandler;

public class JoinLessonActivitySolomon extends AppCompatActivity {
    private EditText studentInputLessonCode;
    private Button btnJoinLesson;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lessonOld);

        //get the lesson code
        studentInputLessonCode = findViewById(R.id.lessonCode);

        //get the lesson button
        btnJoinLesson = findViewById(R.id.btnJoinLesson);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Join Lesson Button Click event
        btnJoinLesson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String lessonCode = studentInputLessonCode.getText().toString().trim();

                if (!lessonCode.isEmpty())
                {
                    //joinLesson(lessonCode);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill in the lesson code", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }


}
