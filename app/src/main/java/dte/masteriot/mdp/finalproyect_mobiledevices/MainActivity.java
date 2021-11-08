package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dte.masteriot.mdp.finalproyect_mobiledevices.mqtt.LoginActivity;
import dte.masteriot.mdp.finalproyect_mobiledevices.mqtt.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private static final String URL_INCIDENTS = "https://informo.madrid.es/informo/tmadrid/incid_aytomadrid.xml";

    Button btIncidents, btStatistics, btForum, btRegister;
    ExecutorService es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btIncidents= (Button) findViewById(R.id.incidents_button);
        btStatistics= (Button) findViewById(R.id.statistics_button);
        btForum= (Button) findViewById(R.id.forum_button);
        btRegister= (Button) findViewById(R.id.forum_register_button);

        toggle_buttons(true);

        es = Executors.newSingleThreadExecutor();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String string_result;
            super.handleMessage(msg);

            if((string_result = msg.getData().getString("content")) != null) {
               newActivity(string_result);
            }
        }
    };

    private void toggle_buttons(boolean state) {
        btIncidents.setEnabled(state);
        btStatistics.setEnabled(state);
        btForum.setEnabled(state);
    }

    public void onIncidentsClick(View view) {
        toggle_buttons(false);
        LoadURLContents loadURLContents = new LoadURLContents(handler, URL_INCIDENTS);
        es.execute(loadURLContents);
    }

    public void onForumClick(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void onRegisterForumClick(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void newActivity (String string_result){
        Intent i = new Intent(this,IncidentsActivity.class);
        i.putExtra("xmlURL", string_result);
        startActivity(i);
    }


}