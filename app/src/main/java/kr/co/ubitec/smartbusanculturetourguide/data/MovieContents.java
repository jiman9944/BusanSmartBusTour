package kr.co.ubitec.smartbusanculturetourguide.data;

/**
 * Created by Administrator on 2017-09-21.
 */

public class MovieContents {
    int conNo;
    double lat;
    double lng;
    double distance;
    String name;

    public MovieContents() {
    }

    public MovieContents(int conNo, double lat, double lng, double distance, String name) {
        this.conNo = conNo;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MovieContents{" +
                "conNo=" + conNo +
                ", lat=" + lat +
                ", lng=" + lng +
                ", distance=" + distance +
                ", name='" + name + '\'' +
                '}';
    }

    public int getConNo() {
        return conNo;
    }

    public void setConNo(int conNo) {
        this.conNo = conNo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
