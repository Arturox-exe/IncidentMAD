package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncidentsActivity extends AppCompatActivity {

    private static final String URL_INCIDENTS = "https://informo.madrid.es/informo/tmadrid/incid_aytomadrid.xml";
    private static final List<Incident> listOfIncidents = new ArrayList<>();

    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;

    HttpURLConnection urlConnection;
    ExecutorService es;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        es = Executors.newSingleThreadExecutor();
        LoadURLContents loadURLContents = new LoadURLContents(handler, URL_INCIDENTS);
        es.execute(loadURLContents);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new MyAdapter(this, listOfIncidents);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            String cod = "";
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
                    else if(elementName.equals("tipoincid")){
                        cod=parser.nextText();
                    }
                    else if(elementName.equals("descripcion")){
                        description=parser.nextText();
                    }
                    else if(elementName.equals("longitud")){
                        Long=parser.nextText();
                    }
                    else if(elementName.equals("latitud")){
                        lat=parser.nextText();
                        listOfIncidents.add(new Incident(name,cod,description));
                    }
                }

                //LatLng coordenates = new LatLng(Double.parseDouble(Long), Double.parseDouble(lat));
                //listOfIncidents.add(new Incident(name,cod,description,coordenates));

                eventType = parser.next();
            }
            recyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
