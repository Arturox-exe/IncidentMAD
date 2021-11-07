package dte.masteriot.mdp.finalproyect_mobiledevices;

import com.google.android.gms.maps.model.LatLng;

public class Incident {
    private final String name;
    private final String codInc;
    private final String description;
    private final LatLng coordinates;

    Incident(String name, String cod, String description, LatLng coordinates) {
        this.name = name;
        this.codInc=cod;
        this.description=description;
        this.coordinates = coordinates;
    }

    String getName() {
        return name;
    }

    public String getCodInc() {
        return codInc;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    boolean isLocationValid() {
        return (coordinates != null);
    }
}
