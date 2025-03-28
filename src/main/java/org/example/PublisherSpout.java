package org.example;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Structura fixa a campurilor publicatiei e: stationid-integer, city-string,
 * temp-integer, rain-double, wind-integer, direction-string, date-data;
 * pentru anumite campuri (stationid, city, direction, date)
 */

public class PublisherSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    private Random random;
    private List<String> cities = List.of("Bucharest", "Cluj", "Iasi", "Timisoara");
    private List<String> directions = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        this.random = new Random();
    }

    /**
     * se pot folosi seturi de valori prestabilite de unde se va alege una la intamplare
     * pentru celelalte campuri se pot stabili limite inferioare si superioare intre care se
     * va alege una la intamplare.
     */

    @Override
    public void nextTuple(){
    // crearea unei noi publicatii folosind clasa
    Publication publication = new Publication(
            random.nextInt(10) + 1, // stationId
            cities.get(random.nextInt(cities.size())), // city
            random.nextInt(40), // temp
            random.nextDouble(), // rain
            random.nextInt(30), // wind
            directions.get(random.nextInt(directions.size())), // direction
            "2023-02-02" // date
    );
        collector.emit(new Values(publication));
}

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("publication"));
    }
}
