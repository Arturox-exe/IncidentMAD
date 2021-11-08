package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IncidentsActivity extends AppCompatActivity implements MyViewHolder.ItemClickListener{

    private RecyclerView recyclerView;
    private MyAdapter recyclerViewAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new MyAdapter( MainActivity.listOfIncidents, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickIncidentsMap (View view){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("type", "Multiple");
        startActivity(mapIntent);
    }

    @Override
    public void onItemClick(int position, View v) {
        Incident incident = MainActivity.listOfIncidents.get(position);
        if (incident.isLocationValid()) {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            mapIntent.putExtra("position", position);
            mapIntent.putExtra("type", "Individual");
            startActivity(mapIntent);
        }
    }
}
