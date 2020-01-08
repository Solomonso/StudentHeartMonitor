package com.example.studentheartmonitor.app;

public class AppConfig {
    // Server user login url
    //Change the ip address to your pc ip address
    //if it doesn't work add :80 at the end of each ip address.

    public static String URL_LOGIN = "http://192.168.178.24/heartrate_api/loginTeacher.php";
    // Server user register url
    public static String URL_REGISTER = "http://192.168.178.24/heartrate_api/registerTeacher.php";


    public static String URL_CREATE_LESSON = "http://192.168.178.17/heartrate_api/createLesson.php";

    public static String URL_JOIN_LESSON = "http://192.168.178.17/heartrate_api/joinLesson.php";

    public static String URL_LOGIN_STUDENT = "http://192.168.178.24/heartrate_api/loginStudent.php";
    // Server user register url
    public static String URL_REGISTER_STUDENT = "http://192.168.178.24/heartrate_api/registerStudent.php";

    //public static String URL_JOIN_LESSON = "http://192.168.178.24/heartrate_api/getCurrentLessonCode";

}
