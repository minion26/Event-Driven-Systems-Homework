package org.example;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * posibilitatea de fixare a: numarului total de mesaje (publicatii, respectiv subscriptii), ponderii pe frecventa campurilor din subscriptii si ponderii operatorilor de egalitate din subscriptii pentru cel putin un camp
 *
 */

public class SubscriptionSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private Random random = new Random();

    private List<String> cities = List.of("Bucharest", "Cluj", "Iasi", "Timisoara");
    private List<String> directions = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");
    private List<String> operators = List.of("=", "!=", "<", "<=", ">", ">=");

    private Map<String, Double> fieldFrequencies;
    private Map<String, Double> equalityFrequencies;

    public SubscriptionSpout(Map<String, Double> fieldFrequencies, Map<String, Double> equalityFrequencies) {
        this.fieldFrequencies = fieldFrequencies;
        this.equalityFrequencies = equalityFrequencies;
    }

    /**
     * Unele campuri pot lipsi; frecventa campurilor prezente trebuie sa fie configurabila
     * @param map
     * @param topologyContext
     * @param spoutOutputCollector
     */
    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
