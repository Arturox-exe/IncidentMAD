package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IncidentsActivity extends AppCompatActivity {

    Intent inputIntent;
    String text;
    TextView tvContent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        tvContent= findViewById(R.id.textView);
        inputIntent = getIntent();

        text=inputIntent.getStringExtra("xmlURL");

        tvContent.setText(text);
    }
}
