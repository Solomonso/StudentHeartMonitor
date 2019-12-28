package com.example.studentheartmonitor.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "heart_rate";

    // Login table name
    private static final String TABLE_USER = "teacher";



    // Login Table Columns names
    private static final String KEY_ID = "teacher_id";
    private static final String KEY_NAME = "teacher_username";
    private static final String KEY_EMAIL = "teacher_email";
    private static final String KEY_UID = "uid";

    //lesson table name
    private static final String TABLE_LESSON = "lesson";

    //lesson columns name
    private static final String KEY_LESSON_ID = "lesson_id";
    private static final String KEY_LESSON_CODE = "lesson_code";
    private static  final String KEY_LESSON_NAME = "lesson_name";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT" + ")";

        String CREATE_LESSON_TABLE = "CREATE TABLE " + TABLE_LESSON + "("
                + KEY_LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LESSON_CODE + " TEXT,"
                + KEY_LESSON_NAME + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_LESSON_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String teacher_username, String teacher_email, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, teacher_username); // Name
        values.put(KEY_EMAIL, teacher_email); // Email
        values.put(KEY_UID, uid); // Password

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Storing user details in database
     * */
    public void addLesson(String lesson_code, String lesson_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LESSON_CODE, lesson_code); // lesson code
        values.put(KEY_LESSON_NAME, lesson_name); // lesson name

        // Inserting Row
        long id = db.insert(TABLE_LESSON, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New lesson inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("teacher_username", cursor.getString(1));
            user.put("teacher_email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting lesson details from database
     **/
    public HashMap<String, String> getUserLessonDetails() {
        HashMap<String, String> lesson = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LESSON + " ORDER BY lesson_id DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            lesson.put("lesson_code", cursor.getString(1));
            lesson.put("lesson_name", cursor.getString(2));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + lesson.toString());

        return lesson;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteLessons() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LESSON, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
