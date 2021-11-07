package dte.masteriot.mdp.finalproyect_mobiledevices;

public class Incident {
    private final String name;
    private final String codInc;
    private final String description;
<<<<<<< HEAD
    private final LatLng coordinates;
=======
   // private final LatLng coordenates;
>>>>>>> parent of 579f7ac ([ADD] Maps activity)

    Incident(String name, String cod, String description, LatLng coordinates) {
        this.name = name;
        this.codInc=cod;
        this.description=description;
<<<<<<< HEAD
        this.coordinates = coordinates;
=======
       // this.coordenates = coordenates;
>>>>>>> parent of 579f7ac ([ADD] Maps activity)
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

<<<<<<< HEAD
    public LatLng getCoordinates() {
        return coordinates;
    }
=======
    /*public LatLng getCoordenates() {
        return coordenates;
    }*/
>>>>>>> parent of 579f7ac ([ADD] Maps activity)

    boolean isLocationValid() {
        return (coordinates != null);
    }
}
