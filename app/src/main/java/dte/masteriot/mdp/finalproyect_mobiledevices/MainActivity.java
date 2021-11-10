package dte.masteriot.mdp.finalproyect_mobiledevices;

import dte.masteriot.mdp.finalproyect_mobiledevices.incidents.Incident;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
import dte.masteriot.mdp.finalproyect_mobiledevices.mqtt.RegisterActivity;


public class MainActivity extends AppCompatActivity {

    private static final String URL_INCIDENTS = "https://informo.madrid.es/informo/tmadrid/incid_aytomadrid.xml";

    ImageButton btIncidents, btStatistics, btForum;
    TextView tv_incidents, tv_statistics, tv_forum;

    public static Bitmap imageAccident, imageClose, imagePollution, imageWorks, imageAlert;

    public static final List<Incident> listOfIncidents = new ArrayList<>();

    ExecutorService es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        es = Executors.newSingleThreadExecutor();
        LoadURLContents loadURLContents = new LoadURLContents(handler, URL_INCIDENTS);
        es.execute(loadURLContents);


        btIncidents = (ImageButton) findViewById(R.id.incidents_button);
        btStatistics = (ImageButton) findViewById(R.id.statistics_button);
        btForum = (ImageButton) findViewById(R.id.forum_button);

        tv_incidents = findViewById(R.id.tv_incidents);
        tv_incidents.bringToFront();
        tv_incidents.setText("CURRENT INCIDENTS IN MADRID");
        tv_statistics = findViewById(R.id.tv_statistics);
        tv_statistics.bringToFront();
        tv_statistics.setText("STATISTICS");
        tv_forum = findViewById(R.id.tv_forum);
        tv_forum.bringToFront();
        tv_forum.setText("FORUM");

        try {
            InputStream is = getAssets().open("accident.png");
            imageAccident = BitmapFactory.decodeStream(is);
            is = getAssets().open("close.png");
            imageClose = BitmapFactory.decodeStream(is);
            is = getAssets().open("pollution.png");
            imagePollution = BitmapFactory.decodeStream(is);
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
            String lat = "";
            String incd_type = "";
            String startDate = "";
            String endDate = "";

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    if (elementName.equals("nom_tipo_incidencia")) {
                        name = parser.nextText();
                    }
                    else if(elementName.equals("descripcion")){
                        description = parser.nextText();
                    }
                    else if(elementName.equals("cod_tipo_incidencia")){
                        incd_type = parser.nextText();
                    }
                    else if(elementName.equals("fh_inicio")){
                        startDate = parser.nextText();
                    }
                    else if(elementName.equals("fh_final")){
                        endDate = parser.nextText();
                    }
                    else if(elementName.equals("longitud")){
                        Long = parser.nextText();
                    }
                    else if(elementName.equals("latitud")){
                        lat = parser.nextText();
                        LatLng coordinates = new LatLng(Double.parseDouble(lat), Double.parseDouble(Long));
                        Incident incident = new Incident(name, description, startDate, endDate, coordinates, incd_type);
                        listOfIncidents.add(incident);
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            AlertDialog.Builder connection = new AlertDialog.Builder(MainActivity.this);
            connection.setMessage("The application needs Internet connection.\nTry again later.");
            connection.setCancelable(true).setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog info = connection.create();
            info.setTitle("CONNECTION FAILED");
            info.show();
            e.toString();
        }
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