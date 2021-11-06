package dte.masteriot.mdp.finalproyect_mobiledevices;

public class Incident {
    private final String name;
    private final String codInc;
    private final String description;
   // private final LatLng coordenates;

    //Incident(String name, String cod, String description, LatLng coordenates) {
    Incident(String name, String cod, String description)  {
        this.name = name;
        this.codInc=cod;
        this.description=description;
       // this.coordenates = coordenates;
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

    /*public LatLng getCoordenates() {
        return coordenates;
    }*/

}
