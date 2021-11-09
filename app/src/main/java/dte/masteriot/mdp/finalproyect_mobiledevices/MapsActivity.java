package dte.masteriot.mdp.finalproyect_mobiledevices;

import dte.masteriot.mdp.finalproyect_mobiledevices.incidents.Incident;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import dte.masteriot.mdp.finalproyect_mobiledevices.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String image;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(MapsActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Incident> listOfIncidents = MainActivity.listOfIncidents;

        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals("Individual")) {
            Incident incident = listOfIncidents.get(intent.getIntExtra("position",0));
            LatLng coordinates = incident.getCoordinates();
            setImage(incident.getType());
            mMap.addMarker(new MarkerOptions().position(coordinates).icon(BitmapDescriptorFactory.fromAsset(image))).setTag(intent.getIntExtra("position",0));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        }
        else if(intent.getStringExtra("type").equals("Multiple")){
            for(int i = 0 ; i < listOfIncidents.size() ; i++){
                Incident incident = listOfIncidents.get(i);
                setImage(incident.getType());
                mMap.addMarker(new MarkerOptions().position(incident.getCoordinates()).icon(BitmapDescriptorFactory.fromAsset(image))).setTag(i);
            }
            mMap.moveCamera(CameraUpdateFactory.zoomTo(12.7f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.43296,-3.69132)));
        }

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog.Builder information = new AlertDialog.Builder(MapsActivity.this);
                Incident incident = MainActivity.listOfIncidents.get((int)marker.getTag());
                String message = "<b>NAME:</b> " + incident.getName() + "<br><br><b>DESCRIPTION:</b> " + incident.getDescription()
                        +"<br><br><b>START DATE:</b> " + incident.getStartDate() + "<br><br><b>END DATE: </b>" + incident.getEndDate();
                information.setMessage(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
                information.setCancelable(true).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog info = information.create();
                info.setTitle("Description of the incident");
                info.show();
                return false;
            }
        });

    }

    private void setImage(String type){
        switch (type){
            case "RMK":
            case "RWK":
            case "RWL":
                image = "works-icon.png";
                break;
            case "ACI":
                image = "accident-icon.png";
                break;
            case "LCS":
                image = "close-icon.png";
                break;
            case "EXS":
                image = "pollution-icon.png";
                break;
            default:
                image = "alert-icon.png";
                break;
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Show the sensor's value in the UI:
            if(10 > (sensorEvent.values[0])) {
                //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else{
                //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}