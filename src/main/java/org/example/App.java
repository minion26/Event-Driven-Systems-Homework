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

    public static void main( String[] args ) throws Exception {

        // precizam frecventa unor campuri
        Map<String, Double> fieldFrequencies = new HashMap<>();
        fieldFrequencies.put("city", 0.9);
        fieldFrequencies.put("temp", 0.8);
        fieldFrequencies.put("wind", 0.7);

        // precizam frecventa pentru egalitate
        Map<String, Double> equalityFrequencies = new HashMap<>();
        equalityFrequencies.put("city", 0.7);



        SubscriptionSpout subscriptionSpout = new SubscriptionSpout(fieldFrequencies, equalityFrequencies);
        PublisherSpout publisherSpout = new PublisherSpout();
        BasicBolt basicBolt = new BasicBolt();


        for(int i = 0; i < 10; i++){
            Map<String, String> subscription = subscriptionSpout.nextTuple();
            Publication publication = publisherSpout.nextTuple();

            basicBolt.execute(subscription);
            basicBolt.execute(publication);

            Thread.sleep(1000);
        }

    }
}
