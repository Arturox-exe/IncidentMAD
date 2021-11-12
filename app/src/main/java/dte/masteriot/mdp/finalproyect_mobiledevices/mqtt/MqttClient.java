package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import dte.masteriot.mdp.finalproyect_mobiledevices.MapsActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class MqttClient extends AppCompatActivity implements SensorEventListener {
    final String serverUri = "tcp://2.137.213.175";
    final String subscriptionTopic = "incidents/madrid";
    final String publishTopic = "incidents/madrid";
    private String currentDateTimeString = "";
    private String new_message = "";
    MqttAndroidClient mqttAndroidClient;
    String clientId = "Client1";
    private HistoryAdapter mAdapter;
    String user;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LatLng position;
    private SensorManager sensorManager;
    private Sensor lightSensor;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(MqttClient.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        locationRequest = LocationRequest.create();
        RecyclerView mRecyclerView = findViewById(R.id.history_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HistoryAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        clientId = clientId + System.currentTimeMillis();

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
        } else {
            getCurrentLocation();
        }

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    subscribeToTopic();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                addToHistory(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri +
                            ". Cause: " + ((exception.getCause() == null)?
                            exception.toString() : exception.getCause()));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            addToHistory(e.toString());
        }
    }

    private void addToHistory(String mainText) {
        System.out.println("LOG: " + mainText);
        mAdapter.add(mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast Tsubscribe = Toast.makeText(getApplicationContext(),"Subscribed to: " + subscriptionTopic, Toast.LENGTH_SHORT);
                    Tsubscribe.show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast Tsubscribe = Toast.makeText(getApplicationContext(),"Failed to subscribe", Toast.LENGTH_SHORT);
                    Tsubscribe.show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast Tsubscribe = Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT);
            Tsubscribe.show();

        }

    }


    public void onPublishMessage(View view) {
        EditText eMessage = (EditText) findViewById(R.id.eMessage);

        if(!eMessage.getText().toString().isEmpty()) {
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            MqttMessage message = new MqttMessage();
            Bundle savedInstanceState = null;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    user= null;
                } else {
                    user= extras.getString("user");
                }
            } else {
                user= (String) savedInstanceState.getSerializable("user");
            }

            String string_position = position.toString();
            String publish = currentDateTimeString + " [" + string_position + "]" + "\n" + "   " + user + ":   " + eMessage.getText().toString();
            eMessage.setText("");
            message.setPayload(publish.getBytes());
            message.setRetained(false);
            message.setQos(0);
            try {
                mqttAndroidClient.publish(publishTopic, message);

            } catch (Exception e) {
                e.printStackTrace();
                addToHistory(e.toString());
            }

            if (!mqttAndroidClient.isConnected()) {
                addToHistory("Client not connected!");
            }
        }

        else{
            Toast Tmessage = Toast.makeText(getApplicationContext(),"Your message can't be empty", Toast.LENGTH_SHORT);
            Tmessage.show();
        }

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult == null){
                return;
            }
            for (Location location : locationResult.getLocations()) {
                position = new LatLng(location.getLatitude(), location.getLongitude());
            }
            fusedLocationProviderClient.removeLocationUpdates(this);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    Log.w("PERMISSION", "permission granted");
                    getCurrentLocation();
                } else {
                    Log.w("PERMISSION", "permission NOT granted");
                }
                return;
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
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
    public void onAccuracyChanged(Sensor sensor, int i) {}
