package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<Incident> incidents;
    Context context;


    public MyAdapter(Context ctxt, List<Incident> listofitems) {
        super();
        context = ctxt;
        incidents = listofitems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method has to actually inflate the item view and return the view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_card, parent, false);
        return new MyViewHolder(context, v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Incident incident = incidents.get(position);
        holder.bindValues(incident);
    }


    @Override
    public int getItemCount() {
        return incidents.size();
    }

}
