package org.example;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {

    private static final String SPOUT_ID_PUBLISHER = "publisher_spout";
    private static final String SPOUT_ID_SUBSCRIPTION = "subscription_spout";
    private static final String BOLT_ID_BASIC = "basic_bolt";


    public static void main( String[] args ) throws Exception {

        // precizam frecventa unor campuri
        Map<String, Double> fieldFrequencies = new HashMap<>();
        fieldFrequencies.put("city", 0.9);
        fieldFrequencies.put("temp", 0.8);
        fieldFrequencies.put("wind", 0.7);

        // precizam frecventa pentru egalitate
        Map<String, Double> equalityFrequencies = new HashMap<>();
        equalityFrequencies.put("city", 0.7);


        // construim topologia
        TopologyBuilder builder = new TopologyBuilder();
        SubscriptionSpout subscriptionSpout = new SubscriptionSpout(fieldFrequencies, equalityFrequencies);
        PublisherSpout publisherSpout = new PublisherSpout();
        BasicBolt basicBolt = new BasicBolt();


        builder.setSpout(SPOUT_ID_PUBLISHER, subscriptionSpout);
        builder.setSpout(SPOUT_ID_SUBSCRIPTION, publisherSpout);
        builder.setBolt(BOLT_ID_BASIC, basicBolt).shuffleGrouping(SPOUT_ID_SUBSCRIPTION).shuffleGrouping(SPOUT_ID_PUBLISHER);


        Config config = new Config();

        LocalCluster cluster = new LocalCluster();
        StormTopology topology = builder.createTopology();

        // fine tuning
        config.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE,1024);
        config.put(Config.TOPOLOGY_TRANSFER_BATCH_SIZE,1);


        cluster.submitTopology("first_topology", config, topology);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // opreste executia unei topologii
        cluster.killTopology("first_topology");
        cluster.shutdown();

        cluster.close();
    }
}
