package org.example;
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
        int totalSubscriptions = 100;

        SubscriptionSpout subscriptionSpout = new SubscriptionSpout(fieldFrequencies, equalityFrequencies, totalSubscriptions);
        PublisherSpout publisherSpout = new PublisherSpout();
        BasicBolt basicBolt = new BasicBolt();

        new ThreadedTask(4, totalSubscriptions, () -> {
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

