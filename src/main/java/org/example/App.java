package org.example;

import org.example.data.Publication;
import org.example.data.Subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        new ThreadedTask(1, 100, () -> {
            basicBolt.execute( publisherSpout.nextTuple() );
            basicBolt.execute( subscriptionSpout.nextTuple() );

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
