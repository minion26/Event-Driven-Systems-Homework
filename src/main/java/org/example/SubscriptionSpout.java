package org.example;

import org.example.data.Subscription;
import org.example.data.SubscriptionData;
import org.example.util.ListUtil;
import org.example.util.MathUtil;

import java.time.LocalDate;
import java.util.*;

/**
 * posibilitatea de fixare a: numarului total de mesaje (publicatii, respectiv subscriptii),
 * ponderii pe frecventa campurilor din subscriptii si ponderii operatorilor de egalitate
 * din subscriptii pentru cel putin un camp
 */
public class SubscriptionSpout {

    private final Random random = new Random();
    private final List<String> cities = List.of("Bucharest", "Cluj", "Iasi", "Timisoara");
    private final List<String> directions = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");
    private final Map<String, Integer> fieldCounts;
    private final Map<String, Integer> equalityCounts;
    private final int totalSubscriptions;
    private int generatedSubscriptions = 0;

    public SubscriptionSpout(Map<String, Double> fieldFrequencies, Map<String, Double> equalityFrequencies, int totalSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
        this.fieldCounts = new HashMap<>();
        this.equalityCounts = new HashMap<>();

        // numarul de subscriptii
        for (String field : fieldFrequencies.keySet()) {
            fieldCounts.put(field, (int) Math.round(fieldFrequencies.get(field) * totalSubscriptions / 100.0));
        }

        // numarul de subscriptii cu operatorul de egalitate
        for (String field : equalityFrequencies.keySet()) {
            equalityCounts.put(field, (int) Math.round(equalityFrequencies.get(field) * totalSubscriptions / 100.0));
        }
    }

    // Constructor that matches the second implementation's signature
    public SubscriptionSpout(Map<String, Double> fieldFrequencies, Map<String, Double> equalityFrequencies) {
        this(fieldFrequencies, equalityFrequencies, 1000); // Default to 1000 subscriptions if not specified
    }

    public Subscription nextTuple() {
        if (generatedSubscriptions >= totalSubscriptions) {
            return null; // am generat toate subscriptiile
        }

        Subscription subscription = new Subscription();

        // adaugarea campurilor
        if (shouldIncludeField("city")) {
            subscription.addData(generateCityData());
        }
        if (shouldIncludeField("temp")) {
            subscription.addData(generateTempData());
        }
        if (shouldIncludeField("wind")) {
            subscription.addData(generateWindData());
        }
        if (shouldIncludeField("rain")) {
            subscription.addData(generateRainData());
        }
        if (shouldIncludeField("direction")) {
            subscription.addData(generateDirectionData());
        }
        if (shouldIncludeField("stationid")) {
            subscription.addData(generateStationIdData());
        }
        if (shouldIncludeField("date")) {
            subscription.addData(generateDateData());
        }

        generatedSubscriptions++;
        return subscription;
    }

    private boolean shouldIncludeField(String field) {
        if (!fieldCounts.containsKey(field) || fieldCounts.get(field) <= 0) {
            return false;
        }

        fieldCounts.put(field, fieldCounts.get(field) - 1);
        return true;
    }

    private SubscriptionData generateCityData() {
        String city = ListUtil.RandomFrom(cities);
        String operator = shouldUseEqualityOperator("city") ? "=" : "!=";
        return new SubscriptionData("city", operator, city);
    }

    private SubscriptionData generateTempData() {
        int temp =  (int) random.nextGaussian() * 15 + 20;
        String operator = shouldUseEqualityOperator("temp") ? "=" : getRandomNonEqualityOperator();
        return new SubscriptionData("temp", operator, String.valueOf(temp));
    }

    private SubscriptionData generateWindData() {
        int wind = random.nextInt(30);
        String operator = shouldUseEqualityOperator("wind") ? "=" : getRandomNonEqualityOperator();
        return new SubscriptionData("wind", operator, String.valueOf(wind));
    }

    private SubscriptionData generateRainData() {
        int rain = (int) random.nextGaussian() * 25 + 50;
        rain = MathUtil.clampInteger(rain, 0, 100);
        String operator = shouldUseEqualityOperator("rain") ? "=" : getRandomNonEqualityOperator();
        return new SubscriptionData("rain", operator, String.format("%.1f", rain));
    }

    private SubscriptionData generateDirectionData() {
        String direction = ListUtil.RandomFrom(directions);
        String operator = shouldUseEqualityOperator("direction") ? "=" : getRandomNonEqualityOperator();
        return new SubscriptionData("direction", operator, direction);
    }

    private SubscriptionData generateStationIdData() {
        int stationId = random.nextInt(10) + 1;
        String operator = shouldUseEqualityOperator("stationid") ? "=" : getRandomNonEqualityOperator();
        return new SubscriptionData("stationid", operator, String.valueOf(stationId));
    }

    private SubscriptionData generateDateData() {
        String date = LocalDate.now().toString();
        String operator = shouldUseEqualityOperator("date") ? "=" : getRandomNonEqualityOperator();
        return new SubscriptionData("date", operator, date);
    }

    private boolean shouldUseEqualityOperator(String field) {
        if (!equalityCounts.containsKey(field) || equalityCounts.get(field) <= 0) {
            return false;
        }

        equalityCounts.put(field, equalityCounts.get(field) - 1);
        return true;
    }

    private String getRandomNonEqualityOperator() {
        List<String> nonEqualityOps = List.of("!=", "<", "<=", ">", ">=");
        return nonEqualityOps.get(random.nextInt(nonEqualityOps.size()));
    }
}