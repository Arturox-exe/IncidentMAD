package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<Incident> incidents;

    private final MyViewHolder.ItemClickListener myClickListener;

    public MyAdapter( List<Incident> listofitems, MyViewHolder.ItemClickListener clickListener) {
        super();
        incidents = listofitems;
        this.myClickListener = clickListener;
    }

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
