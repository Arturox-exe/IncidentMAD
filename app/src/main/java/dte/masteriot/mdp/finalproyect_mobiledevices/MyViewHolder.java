package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    Context context;
    TextView title;
    TextView description;

    public MyViewHolder(Context ctxt, View itemView) {
        super(itemView);
        context = ctxt;
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
    }

    void bindValues(Incident incident) {
        // give values to the elements contained in the item view
        title.setText(incident.getName());
        description.setText(incident.getDescription());
    }

}
