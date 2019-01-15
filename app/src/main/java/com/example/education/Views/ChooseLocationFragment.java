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
import com.example.education.R;
import com.yandex.mapkit.geometry.Point;

public class ChooseLocationFragment extends Fragment implements View.OnClickListener {
    private Point ROUTE_END_LOCATION_STROMYNKA = new Point(55.793288, 37.700819);
    private Point ROUTE_END_LOCATION_VERNADKA = new Point(55.669980, 37.480400);
    private double home1;
    private double home2;
    private Button vernadkaBtn;
    private Button stromynkaBtn;
    private EditText editText1;
    private EditText editText2;
    @Override
    public void onClick(View view){
        Fragment fragment = null;
        Class fragmentClass = null;
        FragmentManager fragmentManager;
        Point HOME_LOCATION = new Point(home1, home2);
        switch(view.getId())
        {
            case R.id.Stromynka:
                fragmentClass = MapFragment.class;
                try{
                    fragment = (Fragment) fragmentClass.newInstance();
                    ((MapFragment) fragment).setROUTE_END_LOCATION(ROUTE_END_LOCATION_STROMYNKA);
                    ((MapFragment) fragment).setROUTE_START_LOCATION(HOME_LOCATION);

                }catch (Exception e){
                    e.printStackTrace();
                }
                fragmentManager = getActivity().getSupportFragmentManager();
                try {
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }catch (Exception e){
                    e.printStackTrace();

                }
                break;
            case R.id.Vernadka:
                fragmentClass = MapFragment.class;
                try{
                    fragment = (Fragment) fragmentClass.newInstance();
                    ((MapFragment) fragment).setROUTE_END_LOCATION(ROUTE_END_LOCATION_VERNADKA);
                    ((MapFragment) fragment).setROUTE_START_LOCATION(HOME_LOCATION);
                }catch (Exception e){
                    e.printStackTrace();
                }
                fragmentManager = getActivity().getSupportFragmentManager();
                try {
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }catch (Exception e){
                    e.printStackTrace();

                }
                break;
            default:
                throw new RuntimeException("Unknow button ID");
        }


    }

    private OnFragmentInteractionListener mListener;

    public ChooseLocationFragment() {
        // Required empty public constructor
    }

    public static ChooseLocationFragment newInstance(String param1, String param2) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
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
        View view = inflater.inflate(R.layout.choose_location_fragment, container, false);
        stromynkaBtn = view.findViewById(R.id.Stromynka);
        vernadkaBtn = view.findViewById(R.id.Vernadka);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        String home1str = editText1.getText().toString();
        String home2str = editText2.getText().toString();
        home1 = Double.valueOf(home1str);
        home2 = Double.valueOf(home2str);
        stromynkaBtn.setOnClickListener(this);
        vernadkaBtn.setOnClickListener(this);
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
