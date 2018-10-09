package com.ysbing.ypermission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

class SensorsTest {

    static boolean check(@NonNull Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            try {
                Sensor heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
                sensorManager.registerListener(SENSOR_EVENT_LISTENER, heartRateSensor, 3);
                sensorManager.unregisterListener(SENSOR_EVENT_LISTENER, heartRateSensor);
            } catch (Throwable e) {
                PackageManager packageManager = context.getPackageManager();
                return !packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE);
            }
        }
        return true;
    }

    private static final SensorEventListener SENSOR_EVENT_LISTENER = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}