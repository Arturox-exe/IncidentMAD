package dte.masteriot.mdp.finalproyect_mobiledevices;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import dte.masteriot.mdp.finalproyect_mobiledevices.incidents.Incident;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import dte.masteriot.mdp.finalproyect_mobiledevices.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String image;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean position_b = false;
    private LatLng coordinates;
    private Polyline polyline;
    private Marker currentPositionMarker;
    private Intent intent;
    private String type = "";
    private LatLng position;
    private float distance;
    TextView tvDistance;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult == null){
                return;
            }
            for(Location location: locationResult.getLocations()){
                position = new LatLng(location.getLatitude(),location.getLongitude());
                currentPositionMarker = mMap.addMarker(new MarkerOptions().position(position).title("Current position"));
                getDistance();
                if(type.equals("Individual")){
                    polyline = mMap.addPolyline(new PolylineOptions().add(position, coordinates));
                }
            }
            fusedLocationProviderClient.removeLocationUpdates(this);

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvDistance = (TextView) findViewById(R.id.distanceId);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationRequest = LocationRequest.create();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Incident> listOfIncidents = MainActivity.listOfIncidents;

        intent = getIntent();
        if(intent.getStringExtra("type").equals("Individual")) {
            type = "Individual";
            Incident incident = listOfIncidents.get(intent.getIntExtra("position",0));
            coordinates = incident.getCoordinates();
            setImage(incident.getType());
            mMap.addMarker(new MarkerOptions().position(coordinates).icon(BitmapDescriptorFactory.fromAsset(image))).setTag(intent.getIntExtra("position",0));
        }
        else if(intent.getStringExtra("type").equals("Multiple")){
            type = "";
            for(int i = 0 ; i < listOfIncidents.size() ; i++){
                Incident incident = listOfIncidents.get(i);
                setImage(incident.getType());
                mMap.addMarker(new MarkerOptions().position(incident.getCoordinates()).icon(BitmapDescriptorFactory.fromAsset(image))).setTag(i);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12.7f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.43296,-3.69132)));
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCurrentPositionClicked (View v){
        if(intent.getStringExtra("type").equals("Individual")) {
            if (!position_b) {
                if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
                } else {
                    getCurrentLocation();
                }
                position_b = true;
            } else {
                polyline.remove();
                currentPositionMarker.remove();
                position_b = false;
            }
        }
        else if(intent.getStringExtra("type").equals("Multiple")){
            if (!position_b) {
                if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
                } else {
                    getCurrentLocation();
                }
                position_b = true;
            } else {
                currentPositionMarker.remove();
                position_b = false;
            }
        }
    }

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
    void getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    void getDistance(){
        Location locPosition = new Location ("Position");
        locPosition.setLatitude(position.latitude);
        locPosition.setLongitude(position.longitude);
        Location locMarker = new Location ("Marker");
        locMarker.setLatitude(coordinates.latitude);
        locMarker.setLongitude(coordinates.longitude);
        distance = locPosition.distanceTo(locMarker)/1000;
        Log.e("SEGUIMIENTO", String.valueOf(distance));
        tvDistance.setText("Distance to the incident: " + distance + " Km");
    }
}