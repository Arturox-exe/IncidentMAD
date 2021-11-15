package dte.masteriot.mdp.IncidentMAD;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import dte.masteriot.mdp.IncidentMAD.incidents.IncidentsActivity;

public class InitScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_init);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(InitScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);

    }
}
