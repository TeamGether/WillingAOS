package com.teamgether.willing.view

import android.Manifest
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.teamgether.willing.R
import kotlinx.android.synthetic.main.activity_check.*


class CheckActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private lateinit var stepCountSensor: Sensor
    var currentSteps = 0// 현재 걸음 수

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        if(ContextCompat.checkSelfPermission(
                this,
                ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 0)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }
        reset_btn.setOnClickListener {
            currentSteps = 0// 현재 걸음 수
            step_count.setText(currentSteps.toString())
        }

        back_btn_ck.setOnClickListener {
            finish()
        }


    }



    override fun onStart() {
        super.onStart()
        if (stepCountSensor != null) {
            sensorManager?.registerListener(this@CheckActivity, stepCountSensor!!, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                if(event.values[0]==1.0f) {
                    currentSteps++
                    step_count.setText(currentSteps.toString())
                }
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}
