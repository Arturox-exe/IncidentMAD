package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncidentsActivity extends AppCompatActivity {

    private static final String URL_INCIDENTS = "https://informo.madrid.es/informo/tmadrid/incid_aytomadrid.xml";
    private static final List<Item> listOfItems = new ArrayList<>();
    private static final String UNKNOWN_NAME = "Unknown name";

    private RecyclerView myRecycleView;

    HttpURLConnection urlConnection;
    ExecutorService es;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        myRecycleView = findViewById(R.id.recyclerView);
        myRecycleView.setLayoutManager(new LinearLayoutManager(this));

        es = Executors.newSingleThreadExecutor();
        LoadURLContents loadURLContents = new LoadURLContents(handler, URL_INCIDENTS);
        es.execute(loadURLContents);
    }

    // Define the handler that will receive the messages from the background thread:
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
            String id = "";
            String cod = "";
            String inDate = "";
            String fiDate = "";
            String description = "";
            String Long = "";
            String lat = "";

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    if (elementName.equals("nom_tipo_incidencia")) {
                        name=parser.nextText();
                    }
                    else if(elementName.equals("id_incidencia")){
                        id=parser.nextText();
                    }
                    else if(elementName.equals("tipoincid")){
                        cod=parser.nextText();
                    }
                    else if(elementName.equals("fh_inicio")){
                        inDate=parser.nextText();
                    }
                    else if(elementName.equals("fh_final")){
                        fiDate=parser.nextText();
                    }
                    else if(elementName.equals("descripcion")){
                        description=parser.nextText();
                    }
                    else if(elementName.equals("longitud")){
                        Long=parser.nextText();
                    }
                    else if(elementName.equals("latitud")){
                        lat=parser.nextText();
                    }
                }
                listOfItems.add(new Item(name,id,cod,inDate,fiDate,description,Long,lat));
                eventType = parser.next(); // Get next parsing event
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
