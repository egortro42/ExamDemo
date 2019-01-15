package com.example.education.Presenters;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.education.Models.AccelerometerModel;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;


public class AccelerometerPresenter {
    private  SensorManager sensorManager;
    private  Sensor sensorAccel;
    private  Sensor sensorLinAccel;
    private  Sensor sensorGravity;
    private  Timer timer;
    AccelerometerModel accelerometer_model = new AccelerometerModel();
    public void setSettings(Activity activity){
        sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLinAccel = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    public void setAccelerometerListenerSettings(){
        sensorManager.registerListener(accelerometer_model.sensorEventListener, sensorAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometer_model.sensorEventListener, sensorLinAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometer_model.sensorEventListener, sensorGravity,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegister(){
        sensorManager.unregisterListener(accelerometer_model.sensorEventListener);
        timer.cancel();
    }

    public void showInfo(final TextView tvText, final Activity activity){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        accelerometer_model.showInfo(tvText);
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);
    }
}
