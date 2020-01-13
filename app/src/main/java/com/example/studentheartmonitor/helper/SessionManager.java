package com.example.studentheartmonitor.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import java.util.HashMap;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public static final String KEY_STUDENT_ID = "Student_id";

    public static final String KEY_TEACHER_ID = "Teacher_id";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSessionStudent(String studentId) {

        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        // Storing id in pref
        editor.putString(KEY_STUDENT_ID, studentId);

        // commit changes
        editor.commit();

        Log.d(TAG, "Student login session modified!");
    }

    public void createSessionTeacher(String teacherId) {

        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        // Storing id in pref
        editor.putString(KEY_TEACHER_ID, teacherId);

        // commit changes
        editor.commit();

        Log.d(TAG, "Teacher login session modified!");
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Main Activity
            Intent intent = new Intent(_context, com.example.studentheartmonitor.MainActivity.class);
            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(intent);
        }

    }

    /**
     * Get stored session data
     * */
    public String getStudentId(){
        String StudentId = pref.getString(KEY_STUDENT_ID, null);

        // return user
        return StudentId;
    }

    public String getTeacherId(){
        String TeacherId = pref.getString(KEY_TEACHER_ID, null);

        // return user
        return TeacherId;
    }

    /**
     * Clear session details
     * */
    public void logOut(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to Main Activity
        Intent i = new Intent(_context, com.example.studentheartmonitor.MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    //session.getStudentId(uid); : for getting data
    //session.setStudentId(uid); : for saving data
}