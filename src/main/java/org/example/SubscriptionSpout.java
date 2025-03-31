package org.example;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.time.LocalDate;
import java.util.HashMap;
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

    private Map<String, Double> fieldFrequencies ;
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
        this.collector = spoutOutputCollector;
    }

    /**
     * random.nextDouble() genereaza un numar intre 0.0 si 1.0
     * fieldFrequencies returneaza probabilitatea ca acest cams sa fie inclus in subscriptie
     *
     */

    @Override
    public void nextTuple() {
        Map<String, String> subscription = new HashMap<>();

        if(random.nextDouble() < fieldFrequencies.getOrDefault("city", 0.0)){
            String city = cities.get(random.nextInt(cities.size()));

            // un oras nu poate fi mai mi decat alt oras deci aleg doar = / !=
            String operator = random.nextDouble() < equalityFrequencies.getOrDefault("city", 0.0) ? "=" : "!=";
            subscription.put("city", operator+ " "+city);
        }

        if(random.nextDouble() < fieldFrequencies.getOrDefault("temp", 0.0)){
            int temp = -20 + random.nextInt(61); //temperaturi intre -20 si 40
            String operator = operators.get(random.nextInt(operators.size()));
            subscription.put("temp", operator + " " + temp);
        }

        if (random.nextDouble() < fieldFrequencies.getOrDefault("rain", 0.0)) {
            int rain = random.nextInt(100); // probabilitate ploaie intre 0 si 100
            String operator = operators.get(random.nextInt(operators.size()));
            subscription.put("rain", operator + " " + rain);
        }

        if (random.nextDouble() < fieldFrequencies.getOrDefault("wind", 0.0)) {
            int wind = random.nextInt(30); // Viteza vant intre 0 si 30
            String operator = operators.get(random.nextInt(operators.size()));
            subscription.put("wind", operator + " " + wind);
        }

        if (random.nextDouble() < fieldFrequencies.getOrDefault("direction", 0.0)) {
            String direction = directions.get(random.nextInt(directions.size()));
            subscription.put("direction", "= " + direction);
        }

        if (random.nextDouble() < fieldFrequencies.getOrDefault("date", 0.0)) {
            String date = LocalDate.now().toString();
            subscription.put("date", "= " + date);
        }


        // Emit subscriptia generata
        collector.emit(new Values(subscription));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("subscription"));

    }
}
