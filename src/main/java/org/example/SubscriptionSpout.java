package org.example;

import java.util.*;

public class SubscriptionSpout {

    private final Random random = new Random();
    private final List<String> cities = List.of("Bucharest", "Cluj", "Iasi", "Timisoara");
    private final List<String> directions = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");
    private final List<String> operators = List.of("=", "!=", "<", "<=", ">", ">=");
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

    public String nextTuple() {
        if (generatedSubscriptions >= totalSubscriptions) {
            return null; // am generat toate subscriptiile
        }

        StringBuilder subscription = new StringBuilder("{");
        List<String> fields = new ArrayList<>();

        // adaugarea campurilor
        if (shouldIncludeField("city")) {
            fields.add(generateCityField());
        }
        if (shouldIncludeField("temp")) {
            fields.add(generateTempField());
        }
        if (shouldIncludeField("wind")) {
            fields.add(generateWindField());
        }
        if (shouldIncludeField("rain")) {
            fields.add(generateRainField());
        }
        if (shouldIncludeField("direction")) {
            fields.add(generateDirectionField());
        }
        if (shouldIncludeField("stationid")) {
            fields.add(generateStationIdField());
        }
        if (shouldIncludeField("date")) {
            fields.add(generateDateField());
        }

        subscription.append(String.join(";", fields));
        subscription.append("}");

        generatedSubscriptions++;
        return subscription.toString();
    }

    private boolean shouldIncludeField(String field) {
        if (!fieldCounts.containsKey(field) || fieldCounts.get(field) <= 0) {
            return false;
        }

        fieldCounts.put(field, fieldCounts.get(field) - 1);
        return true;
    }

    private String generateCityField() {
        String city = cities.get(random.nextInt(cities.size()));
        String operator = shouldUseEqualityOperator("city") ? "=" : "!=";
        return "(city," + operator + ",\"" + city + "\")";
    }

    private String generateTempField() {
        int temp = random.nextInt(61) - 20;
        String operator = shouldUseEqualityOperator("temp") ? "=" : getRandomNonEqualityOperator();
        return "(temp," + operator + "," + temp + ")";
    }

    private String generateWindField() {
        int wind = random.nextInt(30);
        String operator = shouldUseEqualityOperator("wind") ? "=" : getRandomNonEqualityOperator();
        return "(wind," + operator + "," + wind + ")";
    }

    private String generateRainField() {
        double rain = Math.round(random.nextDouble() * 10) / 10.0;
        String operator = shouldUseEqualityOperator("rain") ? "=" : getRandomNonEqualityOperator();
        return "(rain," + operator + "," + String.format("%.1f", rain) + ")";
    }

    private String generateDirectionField() {
        String direction = directions.get(random.nextInt(directions.size()));
        String operator = shouldUseEqualityOperator("direction") ? "=" : getRandomNonEqualityOperator();
        return "(direction," + operator + ",\"" + direction + "\")";
    }

    private String generateStationIdField() {
        int stationId = random.nextInt(10) + 1;
        String operator = shouldUseEqualityOperator("stationid") ? "=" : getRandomNonEqualityOperator();
        return "(stationid," + operator + "," + stationId + ")";
    }

    private String generateDateField() {
        String date = "2023-02-02";
        String operator = shouldUseEqualityOperator("date") ? "=" : getRandomNonEqualityOperator();
        return "(date," + operator + "," + date + ")";
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