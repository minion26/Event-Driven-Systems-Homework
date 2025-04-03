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

    public static void main(String[] args) throws Exception {
        // Configurarea procentelor
        Map<String, Double> fieldFrequencies = new HashMap<>();
        fieldFrequencies.put("city", 90.0);  // 90% din subscriptii au campul city
        fieldFrequencies.put("temp", 80.0);  // 80% din subscriptii au campul temp
        fieldFrequencies.put("wind", 70.0);  // 70% din subscriptii au campul wind

        // frecventa operatorului egal
        Map<String, Double> equalityFrequencies = new HashMap<>();
        equalityFrequencies.put("city", 70.0);  // 70% din campurile city utilizeaza operatorul de egalitate
        // numarul de subscriptii generate
        int totalSubscriptions = 10;

        System.out.println("Generating subscriptions...");
        SubscriptionSpout spout = new SubscriptionSpout(fieldFrequencies, equalityFrequencies, totalSubscriptions);
        String subscription;
        while ((subscription = spout.nextTuple()) != null) {
            System.out.println("Generated subscription: " + subscription);
        }

        System.out.println("\nGenerating publications...");
        PublisherSpout publisherSpout = new PublisherSpout();
        for (int i = 0; i < 5; i++) {
            Publication publication = publisherSpout.nextTuple();
            System.out.println("Generated publication: " + publication);

            // procesarea publicatiilor
            BasicBolt basicBolt = new BasicBolt();
            basicBolt.execute(publication);
        }
    }
}
