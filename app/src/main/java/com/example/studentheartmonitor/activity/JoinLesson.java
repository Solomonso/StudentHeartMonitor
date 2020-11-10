package com.example.studentheartmonitor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.studentheartmonitor.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.studentheartmonitor.app.AppConfig;
import com.example.studentheartmonitor.app.AppController;
import com.example.studentheartmonitor.helper.SQLiteHandler;
import com.example.studentheartmonitor.helper.SessionManager2;

public class JoinLesson extends AppCompatActivity {
    private static final String TAG = JoinLesson.class.getSimpleName();
    private Button btnJoinLesson;
    private EditText inputLessonCode;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager2 session;
    private String student_Id;
    private boolean isCheckIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new SessionManager2(getApplicationContext());
        student_Id = session.getStudentId();

        this.isCheckIn = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lesson);

        inputLessonCode = findViewById(R.id.lessonCodeInput);
        btnJoinLesson =  findViewById(R.id.btnJoinLesson);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching lesson details from SQLite
        //HashMap<String,String> lesson = db.getUserLessonDetails();

        //get lesson code
        //String lessonCodeDb = lesson.get("lesson_code");

        // Login button Click Event
        btnJoinLesson.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                String LessonCode = inputLessonCode.getText().toString().trim();

                // Check for empty data
                if (!LessonCode.isEmpty()) {

                    // Fetching lesson details from SQLite
                    HashMap<String,String> lesson = db.getUserLessonDetails();
                    String currentLessonCode = lesson.get("lesson_code");

                    if(LessonCode.equalsIgnoreCase(currentLessonCode))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Lesson joined successfully!", Toast.LENGTH_LONG)
                                .show();
                        openStudentHome();
                       IsCheckIn checkIn = new IsCheckIn();
                       checkIn.setIsCheckIn(true);
                    }

                    else if(!LessonCode.equalsIgnoreCase((currentLessonCode)))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Unable to  join lesson! code incorrect", Toast.LENGTH_LONG)
                                .show();
                    }
                    //joinLesson(LessonCode);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the lesson code!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    public void openStudentHome()
    {
        Intent intent = new Intent(JoinLesson.this,com.example.studentheartmonitor.activity.StudentActivity.class);
        startActivity(intent);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
