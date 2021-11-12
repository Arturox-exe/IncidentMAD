package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import dte.masteriot.mdp.finalproyect_mobiledevices.MapsActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class RegisterActivity extends AppCompatActivity implements SensorEventListener {

    Button btRegister;
    EditText eRegister, ePassword, eRPassword;
    Boolean letter = false, digit = false;
    private MyOpenHelper db;

    private SensorManager sensorManager;
    private Sensor lightSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(RegisterActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        btRegister = (Button) findViewById(R.id.register_button);

    }

    public void onRegisterClick(View view){
        db = new MyOpenHelper(this);
        eRegister = (EditText) findViewById(R.id.eRegisterUser);
        ePassword = (EditText) findViewById(R.id.ePasswordUser);
        eRPassword = (EditText) findViewById(R.id.eRPasswordUser);

        if(!eRegister.getText().toString().isEmpty() && !ePassword.getText().toString().isEmpty()
                && !eRPassword.getText().toString().isEmpty()) {
            String user = eRegister.getText().toString();
            String password = ePassword.getText().toString();
            String rpassword = eRPassword.getText().toString();

            if(password.length() >= 8) {


                for (int i = 0; i < password.length(); i++) {

                    if (Character.isDigit(password.charAt(i))) {
                        digit = true;

                    }
                    if (Character.isLetter(password.charAt(i))) {
                        letter = true;

                    }

                }

                if (digit && letter) {

                    if (password.equals(rpassword)) {
                        Boolean message = db.insertar(user, password);


                        if (message) {
                            Intent i = new Intent(this, MqttClient.class);
                            i.putExtra("user", user);
                            startActivity(i);
                        } else {
                            Toast Tregister = Toast.makeText(getApplicationContext(), "The user name is already in use, try another", Toast.LENGTH_SHORT);
                            Tregister.show();
                        }
                    } else {
                        Toast Tregister = Toast.makeText(getApplicationContext(), "The password must be equal", Toast.LENGTH_SHORT);
                        Tregister.show();
                    }
                } else {
                    Toast Tregister = Toast.makeText(getApplicationContext(), "The password must have at least 1 letter and 1 number", Toast.LENGTH_SHORT);
                    Tregister.show();
                }
            }

            else{
                Toast Tregister = Toast.makeText(getApplicationContext(),"The password must have at least 8 characters", Toast.LENGTH_SHORT);
                Tregister.show();
            }


        }

        else{
            Toast Tregister = Toast.makeText(getApplicationContext(),"Your user name or password can't be empty", Toast.LENGTH_SHORT);
            Tregister.show();
        }
        digit = false;
        letter = false;
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
