package dte.masteriot.mdp.finalproyect_mobiledevices;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import dte.masteriot.mdp.finalproyect_mobiledevices.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    int position;
    private String description;

    static private final String INTENT_EXTRA_LAT = "LAT";
    static private final String INTENT_EXTRA_LON = "LON";
    static private final String INTENT_EXTRA_ZOOM = "ZOOM";
    static private final String INTENT_EXTRA_TITLE = "TITLE";
    static private final String INTENT_EXTRA_DESCRIPTION = "DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals("Individual")) {
            LatLng coordinates = new LatLng(intent.getDoubleExtra(INTENT_EXTRA_LAT, 0),
                    intent.getDoubleExtra(INTENT_EXTRA_LON, 0));
            mMap.addMarker(new MarkerOptions().position(coordinates).title(intent.getStringExtra(INTENT_EXTRA_TITLE)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
            description = intent.getStringExtra(INTENT_EXTRA_DESCRIPTION);
        }
        else if(intent.getStringExtra("type").equals("Multiple")){
            List<Incident> listOfIncidents = MainActivity.listOfIncidents;
            for(int i=0; i<listOfIncidents.size(); i++){
                Incident incident = listOfIncidents.get(i);
                mMap.addMarker(new MarkerOptions().position(incident.getCoordinates()).title(incident.getName()));
            }
            mMap.moveCamera(CameraUpdateFactory.zoomTo(12.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.416667,-3.703889)));
        }

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

    }

    public static void appendExtraForMarker(Intent intent, LatLng coordinates, String title, boolean zoom, String description) {
        intent.putExtra(INTENT_EXTRA_LAT, coordinates.latitude);
        intent.putExtra(INTENT_EXTRA_LON, coordinates.longitude);
        intent.putExtra(INTENT_EXTRA_TITLE, title);
        intent.putExtra(INTENT_EXTRA_ZOOM, zoom);
        intent.putExtra(INTENT_EXTRA_DESCRIPTION, description);
    }
}