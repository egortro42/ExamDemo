package com.example.education.Presenters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.education.Models.ContactsModel;

import java.util.ArrayList;

public class ContactsPresenter {
    private ContentResolver contentResolver;
    private ContactsModel contacts_model = new ContactsModel();

    public ContactsPresenter(ContentResolver contentResolver){
        setContentResolver(contentResolver);
    }

    public void setContentResolver(ContentResolver contentResolver1) {
        contentResolver = contentResolver1;
    }
    public void LoadContacts(ListView listView, Activity activity){
        ArrayList<String> contacts = contacts_model.getContactsList(contentResolver);
        if (contacts == null){
            Toast.makeText(activity, "В ваших контактах нет старост!",
                    Toast.LENGTH_SHORT).show();
        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (activity, android.R.layout.simple_list_item_1, contacts);
            listView.setAdapter(adapter);
        }

    }

    public void AddNewContact(TextView NameEDT, TextView NumberEDT, ListView listView, Activity activity){
        String Name = NameEDT.getText().toString();
        String Number = NumberEDT.getText().toString();
        if(contacts_model.AddNewContact(Name, Number, contentResolver)){
            Toast.makeText(activity, "Контакт создан",
                    Toast.LENGTH_SHORT).show();
            NameEDT.setText("Имя");
            NumberEDT.setText("Номер");
            LoadContacts(listView, activity);
        }
        else{
            Toast.makeText(activity, "Контакт не создан",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void Call(String itemSelected, Activity activity){
        int n = 0;
        String phoneNumber = "";
        for (int i = 0; i <= itemSelected.length(); i++) {
            if (itemSelected.charAt(i) == ' ') {
                n = i + 1;
                break;
            }
        }
        for (int i = n; i <= itemSelected.length(); i++) {
            phoneNumber = phoneNumber + itemSelected.charAt(i-1);
        }


        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }else{
            Toast.makeText(activity, "Ошибка",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
