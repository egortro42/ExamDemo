package com.example.education.Models;

import android.os.Build;

import java.io.IOException;
import java.io.InputStream;


public class AboutModel {
    public String getInformation() {
        return "***** Информация об устройстве *****" + "\n"
                + "Модель:  " + Build.MODEL + "\n"
                + "Платформа:  " + Build.BOARD + "\n"
                + "Изготовитель:  " + Build.MANUFACTURER + "\n"
                + "Устройство:  " + Build.DEVICE + "\n"
                + "Продукт:  " + Build.PRODUCT + "\n\n\n"
                + "***** Процессор *****" + "\n"
                + "Ядер:  " + Runtime.getRuntime().availableProcessors() + "\n"
                + "Архитектура:  " + System.getProperty("os.arch") + "\n\n\n"
                + "***** Информация об ОС *****" + "\n"
                + "Версия ОС Android:  " + Build.VERSION.RELEASE + "\n"
                + "Патч безопасности:  " + Build.VERSION.SECURITY_PATCH + "\n"
                + "Версия SDK/API:  " + Build.VERSION.SDK_INT + "\n"
                + "Билд:  " + Build.DISPLAY + "\n";
    }
}
