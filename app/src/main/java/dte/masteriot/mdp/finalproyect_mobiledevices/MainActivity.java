package dte.masteriot.mdp.finalproyect_mobiledevices;

import dte.masteriot.mdp.finalproyect_mobiledevices.incidents.Incident;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.model.LatLng;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import dte.masteriot.mdp.finalproyect_mobiledevices.incidents.IncidentsActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.mqtt.LoginActivity;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String URL_INCIDENTS = "https://informo.madrid.es/informo/tmadrid/incid_aytomadrid.xml";
    Button btIncidents, btStatistics, btForum;
    public static Bitmap imageAccident, imageClose, imageDemonstration, imageWorks, imageAlert;
    public static List<Incident> listOfIncidents = new ArrayList<>();

    ExecutorService es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(MainActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        es = Executors.newSingleThreadExecutor();
        LoadURLContents loadURLContents = new LoadURLContents(handler, URL_INCIDENTS);
        es.execute(loadURLContents);

        btIncidents = findViewById(R.id.incidents_button);
        btStatistics = findViewById(R.id.statistics_button);
        btForum = findViewById(R.id.forum_button);

        try {
            InputStream is = getAssets().open("accident.png");
            imageAccident = BitmapFactory.decodeStream(is);
            is = getAssets().open("close.png");
            imageClose = BitmapFactory.decodeStream(is);
            is = getAssets().open("demonstration.png");
            imageDemonstration = BitmapFactory.decodeStream(is);
            is = getAssets().open("works.png");
            imageWorks = BitmapFactory.decodeStream(is);
            is = getAssets().open("alert.png");
            imageAlert = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            parseXml();
        }
    };

    private void parseXml() {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(LoadURLContents.is, null);

            String name = "";
            String description = "";
            String Long = "";
            String lat;
            String incd_type = "";
            String startDate = "";
            String endDate = "";
            listOfIncidents = new ArrayList<>();

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    switch (elementName){
                        case "nom_tipo_incidencia":
                            name = parser.nextText();
                            break;
                        case "descripcion":
                            description = parser.nextText();
                            break;
                        case "cod_tipo_incidencia":
                            incd_type = parser.nextText();
                            break;
                        case "fh_inicio":
                            startDate = parser.nextText();
                            break;
                        case "fh_final":
                            endDate = parser.nextText();
                            break;
                        case "longitud":
                            Long = parser.nextText();
                            break;
                        case "latitud":
                            lat = parser.nextText();
                            LatLng coordinates = new LatLng(Double.parseDouble(lat), Double.parseDouble(Long));
                            Incident incident = new Incident(name, description, startDate, endDate, coordinates, incd_type);
                            listOfIncidents.add(incident);
                            break;
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            AlertDialog.Builder connection = new AlertDialog.Builder(MainActivity.this);
            connection.setMessage("The application needs Internet connection.\nTry again later.");
            connection.setCancelable(false).setNegativeButton("Close app", (dialog, which) -> finish());
            AlertDialog info = connection.create();
            info.setTitle("CONNECTION FAILED");
            info.show();
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

    public void onIncidentsClick(View view) {
        Intent i = new Intent(this, IncidentsActivity.class);
        startActivity(i);
    }

    public void onForumClick(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void onStatisticsClick(View view) {
        Intent i = new Intent(this,StatisticsActivity.class);
        startActivity(i);
    }

}