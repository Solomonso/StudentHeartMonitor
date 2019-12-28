package com.example.studentheartmonitor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.studentheartmonitor.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.studentheartmonitor.MainActivity;
import com.example.studentheartmonitor.app.AppConfig;
import com.example.studentheartmonitor.app.AppController;
import com.example.studentheartmonitor.helper.SQLiteHandler;

public class CreateLessonActivity extends AppCompatActivity {
    private static final String TAG = CreateLessonActivity.class.getSimpleName();
    private EditText inputLessonCode;
    private  EditText inputLessonName;
    private ProgressDialog pDialog;
    private Button btnCreateLesson;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        //get the lesson inputs and button
        inputLessonCode = findViewById(R.id.lessonCode);
        inputLessonName = findViewById(R.id.lessonName);

        btnCreateLesson = findViewById(R.id.btnCreateLesson);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Create Lesson Button Click event
        btnCreateLesson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String lessonCode = inputLessonCode.getText().toString().trim();
                String lessonName = inputLessonName.getText().toString().trim();

                if (!lessonCode.isEmpty() && !lessonName.isEmpty())
                {
                    createLesson(lessonCode,lessonName);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill in the details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    /**
     * Function to store lesson in MySQL database will post params(tag, lessonCode,
     * LessonName) to createLesson url
     * */

    private void createLesson(final String lessonCode, final String lessonName)
    {
        // Tag used to cancel the request
        String tag_string_req = "create_lesson";

        pDialog.setMessage("Creating Lesson...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_CREATE_LESSON, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
//                        // User successfully stored in MySQL
//                        // Now store the user in sqlite
                        JSONObject lesson = jObj.getJSONObject("user");
                        String lesson_code = lesson.getString("lesson_code");
                        String lesson_name = lesson.getString("lesson_name");

                        // Inserting row in users table
                        db.addLesson(lesson_code, lesson_name);
                        Log.d(TAG, "lesson inserted into table");

                        Toast.makeText(getApplicationContext(), "Lesson Successfully created", Toast.LENGTH_LONG).show();

                        // Launch teacher activity
                        Intent intent = new Intent(
                                CreateLessonActivity.this,
                                TeacherActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in creating lesson. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create Lesson Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lesson_code", lessonCode);
                params.put("lesson_name", lessonName);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
