package com.example.education.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.education.Presenters.GitHubRepoPresenter;
import com.example.education.R;

public class GitHubFragment extends Fragment {
    private ListView listView;
    private static final String APP_PREFERENCES = "mysettings";
    private OnFragmentInteractionListener mListener;

    public GitHubFragment() {
    }


    public static GitHubFragment newInstance(String param1, String param2) {
        GitHubFragment fragment = new GitHubFragment();
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

        View view = inflater.inflate(R.layout.git_hub_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.pagination_list);
        SharedPreferences mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        GitHubRepoPresenter gitHub_presenter = new GitHubRepoPresenter(mSettings);
        gitHub_presenter.setListView(listView);
        gitHub_presenter.setActivity(getActivity());
        gitHub_presenter.showPublicGitHubRepo(getContext());
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

