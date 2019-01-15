package com.example.education.Presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.example.education.Models.CameraModel;

import java.io.File;
import java.util.Arrays;

public class CameraPresenter {
    private CameraDevice cameraDevice;
    private Size imageDimension;
    private TextureView textureView;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSessions;
    private CameraModel camera_model;

    public void setTextureView(TextureView textureView){
        this.textureView = textureView;
        camera_model = new CameraModel(textureView);
    }
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            camera_model.setCameraDevice(cameraDevice);
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice=null;
        }
    };
    public void createFolder(String fname, Activity activity){
        String myfolder=Environment.getExternalStorageDirectory()+"/"+fname;
        File f=new File(myfolder);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(activity, myfolder+" Невозможно создать папку", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(activity, myfolder+" Создана папка University", Toast.LENGTH_SHORT).show();
    }

    public void openCamera(Activity activity){
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = camera_model.getCameraId(manager);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                int REQUEST_CAMERA_PERMISSION = 200;
                ActivityCompat.requestPermissions(activity,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CAMERA_PERMISSION);
                return;

            }
            manager.openCamera(cameraId ,stateCallback,null);
        }catch (CameraAccessException e){

        }


    }

    public void createCameraPreview() {
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(imageDimension.getHeight(),imageDimension.getWidth());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(cameraDevice == null)
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null, camera_model.mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void takePicture(Activity activity){
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        if (camera_model.takePicture(activity, manager)){
            createCameraPreview();
            Toast.makeText(activity, "Saved ", Toast.LENGTH_SHORT).show();
        }
    }
    public void stopBackgroundThread(Activity activity){
        if(!camera_model.stopBackgroundThread()){
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void startBackgroundThread(){
        camera_model.startBackgroundThread();
    }


}

