package dte.masteriot.mdp.finalproyect_mobiledevices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Defining the Views
    Button btIncidents, btStatistics, btForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referring the Views
        btIncidents= (Button) findViewById(R.id.incidents_button);
        btStatistics= (Button) findViewById(R.id.statistics_button);
        btForum= (Button) findViewById(R.id.forum_button);
    }

    public void onIncidentsClick(View view) {
        // Implicit Intent:
        Intent i = new Intent(this,IncidentsActivity.class);
        startActivity(i);
    }


}