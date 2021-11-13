package dte.masteriot.mdp.IncidentMAD.incidents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dte.masteriot.mdp.IncidentMAD.R;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    final List<Incident> incidents;

    private final MyViewHolder.ItemClickListener myClickListener;

    public MyAdapter( List<Incident> listofitems, MyViewHolder.ItemClickListener clickListener) {
        super();
        incidents = listofitems;
        this.myClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_card, parent, false);
        return new MyViewHolder(v, myClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindValues(incidents.get(position));
    }


    @Override
    public int getItemCount() {
        return incidents.size();
    }

}
