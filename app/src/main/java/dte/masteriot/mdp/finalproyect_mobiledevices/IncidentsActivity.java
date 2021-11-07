package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class IncidentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;
    Button bIncidentsMap;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        bIncidentsMap= (Button) findViewById(R.id.bIncidentsMap);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new MyAdapter(this, MainActivity.listOfIncidents);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void onClickIncidentsMap (View view){
        Intent i = new Intent (this, MapsActivity.class);
        startActivity(i);
    }

}
