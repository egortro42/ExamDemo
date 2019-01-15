package com.example.education.Models;

import android.os.Build;

public class AboutModel {
    public String getInformation(){
        String information = "Your Android OS version: " + Build.VERSION.SDK_INT + "\n"
                + "BRAND: " + Build.BRAND + "\n"
                + "MODEL: " + Build.MODEL;
        return information;
    }
}
