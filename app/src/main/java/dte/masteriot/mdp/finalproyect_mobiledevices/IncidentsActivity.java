package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.widget.Toast;

=======
>>>>>>> parent of 579f7ac ([ADD] Maps activity)
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

<<<<<<< HEAD
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
            MapsActivity.appendExtraForMarker(mapIntent, incident.getCoordinates(), incident.getName(), true, incident.getDescription());
            mapIntent.putExtra("type", "Individual");
            startActivity(mapIntent);
        }
        else {
            Toast.makeText(this, "Location is unknown", Toast.LENGTH_SHORT).show();
        }
    }

=======
>>>>>>> parent of 579f7ac ([ADD] Maps activity)
}
