package dte.masteriot.mdp.finalproyect_mobiledevices;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    static private final String INTENT_EXTRA_LAT = "LAT";
    static private final String INTENT_EXTRA_LON = "LON";
    static private final String INTENT_EXTRA_ZOOM = "ZOOM";
    static private final String INTENT_EXTRA_TITLE = "TITLE";
    static private final String INTENT_EXTRA_TYPE = "TYPE";
    static private final String INTENT_EXTRA_DESCRIPTION = "DESCRIPTION";
    static private final String INTENT_EXTRA_POSITION = "POSITION";
    private String image;

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
            setImage(intent.getStringExtra(INTENT_EXTRA_TYPE));
            mMap.addMarker(new MarkerOptions().position(coordinates).icon(BitmapDescriptorFactory.fromAsset(image))).setTag(intent.getIntExtra(INTENT_EXTRA_POSITION,0));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        }
        else if(intent.getStringExtra("type").equals("Multiple")){
            List<Incident> listOfIncidents = MainActivity.listOfIncidents;
            for(int i=0; i<listOfIncidents.size(); i++){
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
                String message ="<b>NAME:</b> "+incident.getName()+"<br><br><b>DESCRIPTION:</b> "+incident.getDescription()
                        +"<br><br><b>START DATE:</b> "+"<br><br><b>END DATE:</b>"+"<br><br><b>COORDINATES:</b> ";
                information.setMessage(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
                information.setCancelable(false).setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog info =information.create();
                info.setTitle("Description of the incident");
                info.show();
                return false;
            }
        });

    }

    public static void appendExtraForMarker(Intent intent, LatLng coordinates, String title, boolean zoom, String description, String type, int position) {
        intent.putExtra(INTENT_EXTRA_LAT, coordinates.latitude);
        intent.putExtra(INTENT_EXTRA_LON, coordinates.longitude);
        intent.putExtra(INTENT_EXTRA_TITLE, title);
        intent.putExtra(INTENT_EXTRA_ZOOM, zoom);
        intent.putExtra(INTENT_EXTRA_DESCRIPTION, description);
        intent.putExtra(INTENT_EXTRA_TYPE, type);
        intent.putExtra(INTENT_EXTRA_DESCRIPTION, position);
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
}