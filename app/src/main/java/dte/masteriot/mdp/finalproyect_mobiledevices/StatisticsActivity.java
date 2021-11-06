package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.bendaschel.sevensegmentview.SevenSegmentView;

public class StatisticsActivity extends AppCompatActivity {

    private SevenSegmentView display1, display2, display3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);

        numberOfIncidents();
    }

    public void representationDisplay(int incidents){
        int u,d,c;

        c=incidents/100;
        d=(incidents-(c*100))/10;
        u=(incidents-(c*100+d*10));

        display3.setCurrentValue(c);
        display2.setCurrentValue(d);
        display1.setCurrentValue(u);
    }

    public void numberOfIncidents(){
        int n_incidents=MainActivity.listOfIncidents.size();
        representationDisplay(n_incidents);
    }


}
