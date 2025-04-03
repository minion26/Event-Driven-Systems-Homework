package org.example;



import org.example.data.Subscription;
import org.example.data.SubscriptionData;
import org.example.util.ListUtil;
import org.example.util.MathUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * posibilitatea de fixare a: numarului total de mesaje (publicatii, respectiv subscriptii), ponderii pe frecventa campurilor din subscriptii si ponderii operatorilor de egalitate din subscriptii pentru cel putin un camp
 *
 */

public class SubscriptionSpout  {

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

    public Subscription nextTuple() {
        Subscription subscription = new Subscription();

        newSubscription(subscription, "city", () -> {
            String city = ListUtil.RandomFrom(cities);

            String operator = random.nextDouble() < equalityFrequencies.getOrDefault("city", 0.0) ? "=" : "!=";

            return new SubscriptionData("city", operator, city);
        });

        newSubscription(subscription, "temp", () -> {
            int temp = (int) random.nextGaussian() * 15 + 20;

            String operator = ListUtil.RandomFrom(operators);

            return new SubscriptionData("temp", operator, String.valueOf(temp));
        });

        newSubscription(subscription, "rain", () -> {
            int rain = (int) random.nextGaussian() * 25 + 50;
            rain = MathUtil.clampInteger(rain, 0, 100);

            String operator = ListUtil.RandomFrom(operators);

            return new SubscriptionData("rain", operator, String.valueOf(rain));
        });

        newSubscription(subscription, "wind", () -> {
            int wind = (int) (random.nextGaussian() * 7.5 + 15);
            wind = Math.max(wind, 0);

            String operator = ListUtil.RandomFrom(operators);

            return new SubscriptionData("wind", operator, String.valueOf(wind));
        });

        newSubscription(subscription, "direction", () -> {
            String direction = ListUtil.RandomFrom(directions);

            return new SubscriptionData("direction", "=", direction);
        });

        newSubscription(subscription, "date", () -> {
            String date = LocalDate.now().toString();

            return new SubscriptionData("date", "=", date);
        });

        return subscription;
    }

    private void newSubscription(Subscription subscription, String type, Supplier<SubscriptionData> action) {
        if(Math.abs(random.nextGaussian()) < fieldFrequencies.getOrDefault(type, 0.0)) {
            subscription.addData(action.get());
        }
    }
}
