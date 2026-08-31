package com.example.helveticus;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ImageView bubbleImage;
    private TextView txtAngleX, txtAngleY;
    private float maxRadius = 400f;
    private float sensitivity = 40f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        bubbleImage = findViewById(R.id.bubbleImage);
        txtAngleX = findViewById(R.id.txtAngleX);
        txtAngleY = findViewById(R.id.txtAngleY);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            int angleX = (int) Math.toDegrees(Math.atan2(x, Math.sqrt(y * y + z * z)));
            int angleY = (int) Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));
            txtAngleX.setText("X: " + angleX + "°");
            txtAngleY.setText("Y: " + angleY + "°");

            float tiltX = x * sensitivity;
            float tiltY = -y * sensitivity;

            float tiltMagnitude = (float) Math.sqrt(tiltX * tiltX + tiltY * tiltY);
            float distance = Math.min(tiltMagnitude, maxRadius);

            float directionX = tiltMagnitude == 0 ? 0 : tiltX / tiltMagnitude;
            float directionY = tiltMagnitude == 0 ? 0 : tiltY / tiltMagnitude;

            float translationX = distance * directionX;
            float translationY = distance * directionY;

            bubbleImage.setTranslationX(translationX);
            bubbleImage.setTranslationY(translationY);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}