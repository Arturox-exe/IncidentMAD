package dte.masteriot.mdp.IncidentMAD.incidents;

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

import dte.masteriot.mdp.IncidentMAD.MainActivity;
import dte.masteriot.mdp.IncidentMAD.MapsActivity;
import dte.masteriot.mdp.IncidentMAD.R;

public class IncidentsActivity extends AppCompatActivity implements MyViewHolder.ItemClickListener, SensorEventListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(IncidentsActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MyAdapter recyclerViewAdapter = new MyAdapter( MainActivity.listOfIncidents, this);
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
            if((sensorEvent.values[0]) < 25) {
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
