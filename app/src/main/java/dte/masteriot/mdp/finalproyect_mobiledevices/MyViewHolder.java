package dte.masteriot.mdp.finalproyect_mobiledevices;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView title;
    TextView description;
    ItemClickListener listener;

    public MyViewHolder(View itemView, ItemClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        this.listener = listener;
        description.setOnClickListener(this);
    }

    void bindValues(Incident incident) {
        title.setText(incident.getName());
        description.setText(incident.getDescription());
    }

    @Override
    public void onClick(View view) {
        listener.onItemClick(getAdapterPosition(), view);
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
    }

}
