package dte.masteriot.mdp.IncidentMAD;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import dte.masteriot.mdp.IncidentMAD.incidents.Incident;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.bendaschel.sevensegmentview.SevenSegmentView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements SensorEventListener {

    private SevenSegmentView display1, display2, display3;
    private BarChart chart;
    int works, closeStreet, accidents, demonstration, unknown;
    private List<Incident> listOfIncidents;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(StatisticsActivity.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);

        init_Data();
        listOfIncidents = MainActivity.listOfIncidents;
        numberOfIncidents();
        typeOfIncident();

        chart = findViewById(R.id.chart);
        showGraph();
    }

    public void typeOfIncident(){
        for (int i = 0 ; i < listOfIncidents.size() ; i++){
            switch (listOfIncidents.get(i).getType() ){
                case "RMK":
                case "RWK":
                case "RWL":
                    works++;
                    break;
                case "LCS":
                    closeStreet++;
                    break;
                case "ACI":
                case "ACM":
                case "BKD":
                    accidents++;
                    break;
                case "EVD":
                    demonstration++;
                    break;
                default:
                    unknown++;
                    break;
            }
        }
    }

    public void representationDisplay(int incidents){
        int u,d,c;

        c = incidents / 100;
        d = (incidents - (c * 100)) / 10;
        u = (incidents -(c * 100 + d * 10));

        display3.setCurrentValue(c);
        display2.setCurrentValue(d);
        display1.setCurrentValue(u);
    }

    public void numberOfIncidents(){
        int n_incidents = listOfIncidents.size();
        representationDisplay(n_incidents);
    }

    public void showGraph(){
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(8f);

        chart.getXAxis().setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();
        ArrayList<BarEntry> values4 = new ArrayList<>();
        ArrayList<BarEntry> values5 = new ArrayList<>();

        values1.add(new BarEntry(1, works));
        values2.add(new BarEntry(2, accidents));
        values3.add(new BarEntry(3, closeStreet));
        values4.add(new BarEntry(4, demonstration));
        values5.add(new BarEntry(5, unknown));

        int colorWorks = Color.parseColor("#C17EC8");
        int colorAccident = Color.parseColor("#ED7D31");
        int colorCloseStreet = Color.parseColor("#85C1E9");
        int colorDemonstration = Color.parseColor("#82E0AA");
        int colorUnknown = Color.parseColor("#F7DC6F");

        BarDataSet set1, set2, set3, set4, set5;
        set1 = new BarDataSet(values1, "Works");
        set1.setColor(colorWorks);
        set2 = new BarDataSet(values2, "Accidents");
        set2.setColor(colorAccident);
        set3 = new BarDataSet(values3, "Close Street");
        set3.setColor(colorCloseStreet);
        set4 = new BarDataSet(values4, "Demonstration");
        set4.setColor(colorDemonstration);
        set5 = new BarDataSet(values5, "Unknown");
        set5.setColor(colorUnknown);

        BarData data = new BarData(set1, set2, set3, set4, set5);
        data.setBarWidth(0.9f);
        chart.setData(data);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Show the sensor's value in the UI:
            if((sensorEvent.values[0]) < 25) {
                init_Data();
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                chart.getLegend().setTextColor(Color.WHITE);
                chart.getAxisLeft().setTextColor(Color.WHITE);
            }
            else{
                init_Data();
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void init_Data (){
        listOfIncidents = null;
        works = 0;
        closeStreet = 0;
        accidents = 0;
        demonstration = 0;
        unknown = 0;
    }
}
