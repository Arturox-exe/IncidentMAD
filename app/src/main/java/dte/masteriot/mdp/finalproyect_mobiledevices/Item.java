package dte.masteriot.mdp.finalproyect_mobiledevices;

public class Item {
    private final String name;
    private final String incidentId;
    private final String codInc;
    private final String initDate;
    private final String finalDate;
    private final String description;
    private final String Long;
    private final String lat;

    Item(String name, String id, String cod, String inDate, String fiDate, String description, String Long, String lat) {
        this.name = name;
        this.incidentId=id;
        this.codInc=cod;
        this.initDate=inDate;
        this.finalDate=fiDate;
        this.description=description;
        this.Long=Long;
        this.lat=lat;
    }

    String getName() {
        return name;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public String getCodInc() {
        return codInc;
    }

    public String getInitDate() {
        return initDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLong() {
        return Long;
    }

    public String getLat() {
        return lat;
    }
}
