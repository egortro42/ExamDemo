package com.example.education.Views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.education.Presenters.GitHubRepoPresenter;
import com.example.education.R;

public class ChooseRepoFragment extends Fragment implements View.OnClickListener,
        GitHubFragment.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;
    private Button button;
    private EditText editText;
    private String repo_name = null;

    @Override
    public void onFragmentInteraction(Uri uri){}

    @Override
    public void onClick(View view){
        repo_name = editText.getText().toString();
        GitHubRepoPresenter.setRepo_name(repo_name);
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = GitHubFragment.class;
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        try {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ChooseRepoFragment() {
    }

    public static ChooseRepoFragment newInstance(String param1, String param2) {
        ChooseRepoFragment fragment = new ChooseRepoFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_repo_fragment, container, false);
        button = view.findViewById(R.id.button);
        editText = view.findViewById(R.id.editText);
        editText.setText("elizavetamikhailova");
        button.setOnClickListener(this);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
