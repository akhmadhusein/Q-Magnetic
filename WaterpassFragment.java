package com.husaint2skripsi.lenovo.q_magnetic;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaterpassFragment extends Fragment implements SensorEventListener {

    ImageView ball;
    FrameLayout lyt_tengah;

    Sensor mySensor;
    SensorManager SM;

    int[] img_coordinates = new int[2];
    float timeConstant = 0.18f;
    float alpha = 0.9f;
    float dt = 0;
    // Timestamps for the low-pass filters
    float timestamp = System.nanoTime();
    float timestampOld = System.nanoTime();

    // Gravity and linear accelerations components for the
    // Wikipedia low-pass filter
    private float[] gravity = new float[]
            {0, 0, 0};

    private float[] linearAcceleration = new float[]
            {0, 0, 0};

    // Raw accelerometer data
    private float[] input = new float[]
            {0, 0, 0};

    private int count = 0;


    @Override
    public void onAttach(Context context) {

        // Create our Sensor Manager
        SM = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI);


        super.onAttach(context);
    }


    public WaterpassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waterpass, container, false);

        ball = (ImageView) view.findViewById(R.id.image_ball);
        lyt_tengah = (FrameLayout) view.findViewById(R.id.lyt_1);
        lyt_tengah.getLocationOnScreen(img_coordinates);

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        timestamp = System.nanoTime();

        // Find the sample period (between updates).
        // Convert from nanoseconds to seconds
        dt = 1 / (count / ((timestamp - timestampOld) / 1000000000.0f));

        count++;

        alpha = timeConstant / (timeConstant + dt);

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        float a= event.values[0]*30;
        float b= event.values[1]*40;
        float c= event.values[2];

        linearAcceleration[0] = a - gravity[0];
        linearAcceleration[1] = b - gravity[1];
        linearAcceleration[2] = c - gravity[2];

        float x = (int)linearAcceleration[0];
        float y = (int)linearAcceleration[1];
        float z = (int)linearAcceleration[2];

        //layout tengah
        double x_center = (double)img_coordinates[0]+lyt_tengah.getWidth()/2.0;
        double y_center = (double)img_coordinates[1]+lyt_tengah.getHeight()/2.0;

        String x_center1 = String.valueOf(x_center);
        Float x_center2 = Float.parseFloat(x_center1);

        String y_center1 = String.valueOf(y_center);
        Float y_center2 = Float.parseFloat(y_center1);

        //gerak y ball
        double ball_center = ball.getWidth()/2.0;
        double ball_centery = ball.getHeight()/2.0;

        // perubahan jenis ball
        String ball_center1 = String.valueOf(ball_center);
        Float ball_center2 = Float.parseFloat(ball_center1);

        String ball_centery1 = String.valueOf(ball_centery);
        Float ball_centery2 = Float.parseFloat(ball_centery1);

        //set ball
        ball.setX(x_center2 + x - ball_center2);
        ball.setY(y_center2 - y - ball_centery2);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
