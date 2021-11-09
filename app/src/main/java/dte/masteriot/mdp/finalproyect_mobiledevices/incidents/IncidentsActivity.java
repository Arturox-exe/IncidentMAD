package dte.masteriot.mdp.finalproyect_mobiledevices.incidents;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dte.masteriot.mdp.finalproyect_mobiledevices.MainActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.MapsActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class IncidentsActivity extends AppCompatActivity implements MyViewHolder.ItemClickListener, SensorEventListener {

    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(IncidentsActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new MyAdapter( MainActivity.listOfIncidents, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickIncidentsMap (View view){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("type", "Multiple");
        startActivity(mapIntent);
    }

    @Override
    public void onItemClick(int position, View v) {
        Incident incident = MainActivity.listOfIncidents.get(position);
        if (incident.isLocationValid()) {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            mapIntent.putExtra("position", position);
            mapIntent.putExtra("type", "Individual");
            startActivity(mapIntent);
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
