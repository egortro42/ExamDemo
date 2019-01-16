package com.example.education.Models;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CameraModel {
    private final SparseIntArray ORIENTATIONS = new SparseIntArray();
    {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }
    private boolean isPictureTaken;
    private CameraDevice cameraDevice;
    private File file;
    public Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private TextureView textureView;
    public CameraModel(TextureView textureView){
        this.textureView = textureView;
    }


    public void setCameraDevice(CameraDevice cameraDevice1){
        cameraDevice = cameraDevice1;
    }

    public Size[] getJpegSize(CameraCharacteristics characteristics){
        Size[] jpegSizes = null;
        if(characteristics != null)
            jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG);
        return jpegSizes;
    }

    public int getWidth(Size[] jpegSizes){
        int width;
        if(jpegSizes != null && jpegSizes.length > 0)
        {
            width = jpegSizes[0].getWidth();
            return width;
        }else {
            width = 640;
            return width;
        }
    }

    public int getHeight(Size [] jpegSizes){
        int height;
        if(jpegSizes != null && jpegSizes.length > 0)
        {
            height = jpegSizes[0].getWidth();
            return height;
        }else {
            height = 640;
            return height;
        }
    }

    public  List<Surface> getOutputSurface(ImageReader reader){
        List<Surface> outputSurface = new ArrayList<>(2);
        outputSurface.add(reader.getSurface());
        outputSurface.add(new Surface(textureView.getSurfaceTexture()));
        return outputSurface;
    }

    public CaptureRequest.Builder getCaptureRequestBuilder(ImageReader reader, Activity activity){
        try {
            CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));
            return captureBuilder;
        }catch (CameraAccessException e){
            return null;
        }
    }

    public ImageReader.OnImageAvailableListener getOnImageAvailableListener(final ImageReader reader){
        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = null;
                try{
                    image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    save(bytes);

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally {
                    {
                        if(image != null)
                            image.close();
                    }
                }
            }

            private void save(byte[] bytes) throws IOException {
                OutputStream outputStream = null;
                try{
                    outputStream = new FileOutputStream(file);
                    outputStream.write(bytes);
                }finally {
                    if(outputStream != null)
                        outputStream.close();
                }
            }
        };
        return readerListener;
    }


    public boolean takePicture(final Activity activity, CameraManager manager) {

        if(cameraDevice == null)
            isPictureTaken = false;
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = getJpegSize(characteristics);

            int width = getWidth(jpegSizes);
            int height = getHeight(jpegSizes);

            final ImageReader reader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurface = getOutputSurface(reader);

            final CaptureRequest.Builder captureBuilder = getCaptureRequestBuilder(reader, activity);
            File pictures = Environment.getExternalStoragePublicDirectory("Study");
            file = new File(pictures, UUID.randomUUID().toString() + ".jpg");
            ImageReader.OnImageAvailableListener readerListener = getOnImageAvailableListener(reader);

            reader.setOnImageAvailableListener(readerListener,mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener =
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);
                            isPictureTaken = true;
                        }
                    };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.capture(captureBuilder.build(),captureListener,mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        isPictureTaken = false;
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    isPictureTaken = false;
                }
            },mBackgroundHandler);

            isPictureTaken = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            isPictureTaken = false;
        }
        return isPictureTaken;
    }



    public String getCameraId(CameraManager manager){
        try {
            String cameraId = manager.getCameraIdList()[0];
            return cameraId;
        }catch (CameraAccessException e){
            return null;
        }
    }


    public boolean stopBackgroundThread() {
        boolean check;
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread= null;
            mBackgroundHandler = null;
            check = true;
            return check;
        } catch (InterruptedException e) {
            e.printStackTrace();
            check = false;
            return check;
        }
    }

    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }


}
