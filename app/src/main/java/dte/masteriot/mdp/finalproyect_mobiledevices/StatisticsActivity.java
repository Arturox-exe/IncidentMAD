package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.bendaschel.sevensegmentview.SevenSegmentView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private SevenSegmentView display1, display2, display3;
    private BarChart chart;
    int works, closeStreet, accidents, event, pollution, planned, unknown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);

        numberOfIncidents();
        typeOfIncident();

        chart = findViewById(R.id.chart);
        showGraph();
    }

    public void typeOfIncident(){
        List<Incident> listOfIncidents= MainActivity.listOfIncidents;
        for (int i=0; i< listOfIncidents.size(); i++){
            switch (listOfIncidents.get(i).getCodInc() ){
                case "1":
                    works++;
                    break;
                case "2":
                    accidents++;
                    break;
                case "3":
                    closeStreet++;
                    break;
                case "4":
                    event++;
                    break;
                case "5":
                    planned++;
                    break;
                case "10":
                    pollution++;
                    break;
                default:
                    unknown++;
                    break;
            }
        }
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
        leftAxis.setDrawAxisLine(false);;
        chart.getAxisRight().setEnabled(false);


        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();
        ArrayList<BarEntry> values4 = new ArrayList<>();
        ArrayList<BarEntry> values5 = new ArrayList<>();
        ArrayList<BarEntry> values6 = new ArrayList<>();
        ArrayList<BarEntry> values7 = new ArrayList<>();
        BarDataSet set1, set2, set3, set4, set5, set6, set7;

        values1.add(new BarEntry(1, works));
        values2.add(new BarEntry(2, accidents));
        values3.add(new BarEntry(3, closeStreet));
        values4.add(new BarEntry(4, event));
        values5.add(new BarEntry(5, planned));
        values6.add(new BarEntry(6, pollution));
        values7.add(new BarEntry(7, unknown));

        int colorWorks = Color.parseColor("#FFBB86FC");
        int colorAccident = Color.parseColor("#FF03DAC5");
        int colorCloseStreet = Color.parseColor("#FF3700B3");
        int colorEvent = Color.parseColor("#FF018786");
        int colorPlanned= Color.parseColor("#FF5733");
        int colorPollution = Color.parseColor("#33FF60");
        int colorUnknown = Color.parseColor("#33A1FF");

        set1 = new BarDataSet(values1, "Works");
        set1.setColor(colorWorks);
        set2 = new BarDataSet(values2, "Accidents");
        set2.setColor(colorAccident);
        set3 = new BarDataSet(values3, "Close Street");
        set3.setColor(colorCloseStreet);
        set4 = new BarDataSet(values4, "Event");
        set4.setColor(colorEvent);
        set5 = new BarDataSet(values5, "Planned Incidents");
        set5.setColor(colorPlanned);
        set6 = new BarDataSet(values6, "Pollution");
        set6.setColor(colorPollution);
        set7 = new BarDataSet(values7, "Unknown");
        set7.setColor(colorUnknown);

        BarData data = new BarData(set1, set2, set3, set4, set5, set6, set7);
        data.setBarWidth(0.9f);
        chart.setData(data);
    }
}
