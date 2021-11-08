package dte.masteriot.mdp.finalproyect_mobiledevices;

import com.google.android.gms.maps.model.LatLng;

public class Incident {
    private final String name;
    private final String description;
    private String startDate;
    private String endDate;
    private final LatLng coordinates;
    private final String incd_type;

    Incident(String name, String description, String startDate, String endDate, LatLng coordinates, String incd_type) {
        this.name = name;
        this.description = description;
        this.incd_type = incd_type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coordinates = coordinates;
        date();
    }

    public void date(){
        String day, month, year;
        startDate = startDate.substring(startDate.indexOf("2"),startDate.indexOf("T"));
        endDate = endDate.substring(endDate.indexOf("2"),endDate.indexOf("T"));
        year = startDate.substring(0,4);
        month = startDate.substring(5,7);
        day = startDate.substring(8,10);
        startDate = day + "-" + month + "-" + year;
        year = endDate.substring(0,4);
        month = endDate.substring(5,7);
        day = endDate.substring(8,10);
        endDate = day + "-" + month + "-" + year;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() { return incd_type; }

    public String getStartDate() { return startDate; }

    public String getEndDate() { return endDate; }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public boolean isLocationValid() {
        return (coordinates != null);
    }
}
