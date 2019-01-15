package com.example.education.Presenters;

import com.example.education.Models.AboutModel;

public class AboutPresenter {
    public String getInformation(){
        AboutModel about_model = new AboutModel();
        String information = about_model.getInformation();
        if (information != null){
            return information;
        }
        return "Информация недоступна";
    }
}
