package id.its.ggsensor.sensortreking;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.opencsv.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class homescreen extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private boolean lightStatus;
    boolean mulaiSensor = false;

    TextView x;
    TextView y;
    TextView z;

    TextView lightT;

    String sx, sy, sz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        x = (TextView) findViewById(R.id.xVal);
        y = (TextView) findViewById(R.id.yVal);
        z = (TextView) findViewById(R.id.zVal);
        lightT = (TextView) findViewById(R.id.lightText);
        lightStatus = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor
                (Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

        final Button startBtn = (Button) findViewById(R.id.btnStart);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                mulaiSensor = true;
            }
        });

        final Button stopBtn = (Button) findViewById(R.id.btnStop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                mulaiSensor = false;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        File sdCard = Environment.getExternalStorageDirectory();
        //File dir = new File (sdCard.getAbsolutePath() + "");
        //dir.mkdirs();
        //File file = new File(dir, "filename");

        //FileOutputStream f = new FileOutputStream(file);

        CSVWriter writer = null;

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float light = event.values[0];

            String txt = "Light : " + light;

            lightT.setText(Html.fromHtml(txt));

            if (light < 22.93) {
                lightStatus = true;
            } else {
                lightStatus = false;
            }

        }

        if(mulaiSensor == true) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                float xVal = event.values[0];
                float yVal = event.values[1];
                float zVal = event.values[2];

                sx = "X Value : <font color = '#800080'> " + xVal + "</font>";
                sy = "Y Value : <font color = '#800080'> " + yVal + "</font>";
                sz = "Z Value : <font color = '#800080'> " + zVal + "</font>";

                x.setText(Html.fromHtml(sx));
                y.setText(Html.fromHtml(sy));
                z.setText(Html.fromHtml(sz));
                String coba = xVal+"#"+yVal+"#"+zVal;
                try {
                    writer = new CSVWriter(new FileWriter(sdCard.getAbsolutePath()+"/myfile.csv", true), ',');
                    String[] entries = coba.split("#"); // array of your values
                    writer.writeNext(entries);
                    writer.close();
                }
                catch (IOException e) {
                    //error
                }

            }
        }

    }
}
