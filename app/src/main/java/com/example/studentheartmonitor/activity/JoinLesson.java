package com.example.studentheartmonitor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.studentheartmonitor.R;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentheartmonitor.app.AppConfig;
import com.example.studentheartmonitor.app.AppController;
import com.example.studentheartmonitor.helper.SQLiteHandler;
import com.example.studentheartmonitor.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinLesson extends AppCompatActivity {
    private static final String TAG = JoinLesson.class.getSimpleName();
    private Button btnJoinLesson;
    private EditText inputLessonCode;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private String student_Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());
        student_Id = session.getStudentId();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lesson);

        inputLessonCode = findViewById(R.id.lessonCodeInput);
        btnJoinLesson =  findViewById(R.id.btnJoinLesson);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Login button Click Event
        btnJoinLesson.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                String LessonCode = inputLessonCode.getText().toString().trim();

                // Check for empty data
                if (!LessonCode.isEmpty()) {
                    joinLesson(LessonCode);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the lesson code!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }
    /**
     * function to verify lesson code in mysql db
     * */
    private void joinLesson(final String LessonCode) {
        // Tag used to cancel the request

        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_JOIN_LESSON, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Launch main activity
                        StudentActivity.isCheckIn = true;
                        Intent intent = new Intent(JoinLesson.this, StudentActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lesson_code", LessonCode);
                params.put("student_id", student_Id);

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
