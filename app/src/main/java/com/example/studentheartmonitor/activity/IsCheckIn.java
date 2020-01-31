package com.example.studentheartmonitor.activity;

public class IsCheckIn {
    private static boolean CheckIn;

//    public IsCheckIn()
//    {
//        IsCheckIn = false;
//    }
//
//    public  boolean getCheckIn(boolean isCheckIn)
//    {
//        IsCheckIn = isCheckIn;
//        return  IsCheckIn;
//    }

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
