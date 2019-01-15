package com.example.education.Models;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsModel {
    private final String CONTACT_ID = ContactsContract.Contacts._ID;
    private final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private final String Starosta = "Староста";

    public HashMap<Integer, ArrayList<String>> getPhonesMap(Cursor phoneCur){
        HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
        while (phoneCur.moveToNext()) {
            Integer contactId = phoneCur.getInt(phoneCur.getColumnIndex(PHONE_CONTACT_ID));
            ArrayList<String> curPhones = new ArrayList<>();
            if (phones.containsKey(contactId)) {
                curPhones = phones.get(contactId);
            }
            curPhones.add(phoneCur.getString(0));
            phones.put(contactId, curPhones);
        }
        return phones;
    }

    public void putNote (Cursor noteCur,  HashMap<Integer, String> notes){
        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
        Integer contactId = noteCur.getInt(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.CONTACT_ID));
        if (note != null) {
            if (note.equals(Starosta)) {
                notes.put(contactId, note);
            }
        }
    }
    public ArrayList<String> getContactsList(ContentResolver contentResolver) {
        Cursor phoneCur = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if (phoneCur != null) {
            if (phoneCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = getPhonesMap(phoneCur);
                Cursor nameCur = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,
                        DISPLAY_NAME + " ASC");
                if (nameCur != null) {
                    if (nameCur.getCount() > 0) {
                        ArrayList<String> contacts = new ArrayList<>();
                        HashMap<Integer, String> notes = new HashMap<>();
                        while (nameCur.moveToNext()) {
                            int id = nameCur.getInt(nameCur.getColumnIndex(CONTACT_ID));
                            String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                            String[] noteWhereParams = new String[]{Integer.toString(id),
                                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                            Cursor noteCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                            if (noteCur.moveToFirst()) {
                                putNote(noteCur, notes);
                            }
                            if (phones.containsKey(id) & notes.containsKey(id)) {
                                String contact = nameCur.getString(nameCur.getColumnIndex(DISPLAY_NAME)) + "  ";
                                contact = contact + TextUtils.join(",", phones.get(id).toArray());
                                contacts.add(contact);
                            }
                        }
                        return contacts;
                    }
                    nameCur.close();
                }
            }
            phoneCur.close();
        }
        return null;
    }

    public boolean AddNewContact(String name, String number, ContentResolver contentResolver) {
        ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
        boolean isContactAdded;
        op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, "Староста")
                .build());

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, op);
            isContactAdded = true;
        } catch (Exception e) {
            isContactAdded = false;
        }
        return isContactAdded;
    }



}

