package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

import dte.masteriot.mdp.finalproyect_mobiledevices.MapsActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    Button btLogin;
    EditText eLogin, ePassword;
    private MyOpenHelper db;
    private User userdb;
    private SensorManager sensorManager;
    private Sensor lightSensor;

    private ArrayList<User> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(LoginActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);



        btLogin= (Button) findViewById(R.id.login_button);

    }

    public void onLoginClick(View view){
        db = new MyOpenHelper(this);
        eLogin = (EditText) findViewById(R.id.eUser);
        ePassword = (EditText) findViewById(R.id.ePassword);


        if(!eLogin.getText().toString().isEmpty()) {
            String user = eLogin.getText().toString();
            String password = ePassword.getText().toString();

            userdb = db.query(user);


            if(userdb != null) {
                if(userdb.getPassword().equals(password)) {
                    Intent i = new Intent(this, MqttClient.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
                else{
                    Toast Tlogin = Toast.makeText(getApplicationContext(),"The name or the password is incorrect", Toast.LENGTH_SHORT);
                    Tlogin.show();
                }
            }

            else{
                Toast Tlogin = Toast.makeText(getApplicationContext(),"The name or the password is incorrect", Toast.LENGTH_SHORT);
                Tlogin.show();
            }

        }
        else{
            Toast Tlogin = Toast.makeText(getApplicationContext(),"Your user name can't be empty", Toast.LENGTH_SHORT);
            Tlogin.show();
        }

    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Show the sensor's value in the UI:
            if(10 > (sensorEvent.values[0])) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else{
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
