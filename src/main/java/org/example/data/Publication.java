package org.example.data;

/**
 * Publicatie:
 * {(stationid,1);(city,"Bucharest");(temp,15);(rain,0.5);(wind,12);(direction,"NE");(date,2.02.2023)}
 */
public class Publication {
    int stationId;
    String city;
    int temp;
    double rain;
    int wind;
    String direction;
    String date;

    public Publication(int stationId, String city, int temp, double rain, int wind, String direction, String date) {
        this.stationId = stationId;
        this.city = city;
        this.temp = temp;
        this.rain = rain;
        this.wind = wind;
        this.direction = direction;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("{(stationid,%d);(city,\"%s\");(temp,%d);(rain,%.1f);(wind,%d);(direction,\"%s\");(date,%s)}",
                stationId, city, temp, rain, wind, direction, date);
    }
}
