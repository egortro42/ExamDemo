package com.example.education.Views;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.education.Presenters.AccelerometerPresenter;
import com.example.education.Presenters.CameraPresenter;
import com.example.education.R;


public class AcceleCameraFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextureView textureView;
    AccelerometerPresenter accelerometer_presenter = new AccelerometerPresenter();
    CameraPresenter camera_presenter = new CameraPresenter();

    private TextView tvText;

    public AcceleCameraFragment() {}


    public static AcceleCameraFragment newInstance(String param1, String param2) {
        AcceleCameraFragment fragment = new AcceleCameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public  TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            camera_presenter.createFolder("University", getActivity());
            camera_presenter.openCamera(getActivity());
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accele_camera_fragment, container, false);
        textureView = view.findViewById(R.id.textureView);
        camera_presenter.setTextureView(textureView);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        Button btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_presenter.takePicture(getActivity());
            }
        });

        //Акселерометр
        tvText = view.findViewById(R.id.tvText);
        accelerometer_presenter.setSettings(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        camera_presenter.startBackgroundThread();
        if(textureView.isAvailable())
            camera_presenter.openCamera(getActivity());
        else
            textureView.setSurfaceTextureListener(textureListener);

        accelerometer_presenter.setAccelerometerListenerSettings();
        accelerometer_presenter.showInfo(tvText, getActivity());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int REQUEST_CAMERA_PERMISSION = 200;
        if(requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(), "You can't use camera without permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onPause() {
        camera_presenter.stopBackgroundThread(getActivity());
        super.onPause();
        accelerometer_presenter.unRegister();
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