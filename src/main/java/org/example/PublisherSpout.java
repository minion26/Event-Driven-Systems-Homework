package org.example;

import org.example.data.Publication;

import java.util.List;
import java.util.Random;

/**
 * Structura fixa a campurilor publicatiei e: stationid-integer, city-string,
 * temp-integer, rain-double, wind-integer, direction-string, date-data;
 * pentru anumite campuri (stationid, city, direction, date)
 */

public class PublisherSpout {


    private Random random = new Random();
    private List<String> cities = List.of("Bucharest", "Cluj", "Iasi", "Timisoara");
    private List<String> directions = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");

    public synchronized Publication nextTuple() {
        return new Publication(
                random.nextInt(10) + 1, // stationId
                cities.get(random.nextInt(cities.size())), // city
                random.nextInt(40), // temp
                random.nextDouble(), // rain
                random.nextInt(30), // wind
                directions.get(random.nextInt(directions.size())), // direction
                "2023-02-02" // date
        );
    }
}
