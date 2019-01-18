package com.example.education.Views;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.education.Presenters.ContactsPresenter;
import com.example.education.R;

public class ContactsFragment extends Fragment implements View.OnClickListener{


    private OnFragmentInteractionListener mListener;
    private Button addContactBtn;
    private ListView contactListView;
    private EditText NameEDT;
    private EditText NumberEDT;
    private ContactsPresenter contacts_presenter;


    public ContactsFragment() {}

//    public static ContactsFragment newInstance(String param1, String param2) {
//        ContactsFragment fragment = new ContactsFragment();
//        Bundle args = new Bundle();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);
        ContentResolver contentResolver = getActivity().getContentResolver();

        contacts_presenter = new ContactsPresenter(contentResolver);
        contactListView = (ListView) view.findViewById(R.id.contactList);
        addContactBtn = view.findViewById(R.id.addContact);
        NameEDT = view.findViewById(R.id.Name);
        NumberEDT = view.findViewById(R.id.Number);
        addContactBtn.setOnClickListener(this);
        if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            contacts_presenter.LoadContacts(contactListView, getActivity());
        }


        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                contacts_presenter.Call(((TextView) view).getText().toString(), getActivity());
            }
        });

        contactListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                contacts_presenter.Call(((TextView) view).getText().toString(), getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},2 );
        } else {
            contacts_presenter.AddNewContact(NameEDT, NumberEDT, contactListView, getActivity());
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            contacts_presenter.LoadContacts(contactListView, getActivity());
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



}