package dte.masteriot.mdp.IncidentMAD.incidents;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import dte.masteriot.mdp.IncidentMAD.MainActivity;
import dte.masteriot.mdp.IncidentMAD.R;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView title;
    TextView description;
    ItemClickListener listener;
    ImageView imageView;

    public MyViewHolder(View itemView, ItemClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        imageView = itemView.findViewById(R.id.ImageView);
        this.listener = listener;
        description.setOnClickListener(this);
        title.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    void bindValues(Incident incident) {
        title.setText(incident.getName());
        description.setText(incident.getDescription());
        setImage(incident);
    }

    @Override
    public void onClick(View view) {
        listener.onItemClick(getAdapterPosition(), view);
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
    }

    void setImage(Incident incident){
        switch (incident.getType()){
            case "RMK":
            case "RWK":
            case "RWL":
                imageView.setImageBitmap(MainActivity.imageWorks);
                break;
            case "ACI":
            case "ACM":
            case "BKD":
                imageView.setImageBitmap(MainActivity.imageAccident);
                break;
            case "LCS":
                imageView.setImageBitmap(MainActivity.imageClose);
                break;
            case "EVD":
                imageView.setImageBitmap(MainActivity.imageDemonstration);
                break;
            default:
                imageView.setImageBitmap(MainActivity.imageAlert);
                break;
        }
    }

}
