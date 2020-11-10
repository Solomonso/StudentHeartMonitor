package com.example.studentheartmonitor.activity;

public class IsCheckIn {
    private static boolean CheckIn;

    public IsCheckIn()
    {
        CheckIn = false;
    }

    public  boolean getCheckIn()
    {
        //IsCheckIn = isCheckIn;
        return  CheckIn;
    }

    public  void setIsCheckIn(boolean isCheckIn)
    {
        CheckIn = isCheckIn;
    }
}
