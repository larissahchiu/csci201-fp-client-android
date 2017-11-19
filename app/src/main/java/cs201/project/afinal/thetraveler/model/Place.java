package cs201.project.afinal.thetraveler.model;

/**
 * Created by larissachiu on 11/18/17.
 */

import java.io.Serializable;

public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double lat;
    private double lon;
    private int points;
    private int numVisits;

    public Place(String id, String name, double lat, double lon, int points, int numVisits){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.points = points;
        this.numVisits = numVisits;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setNumVisits(int numVisits) {
        this.numVisits = numVisits;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public int getPoints() {
        return points;
    }
    public int getNumVisits() {
        return numVisits;
    }

    public void incNumOfVisit() {
        numVisits++;
    }

}
