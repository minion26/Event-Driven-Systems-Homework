package org.example.data;

public class SubscriptionData {
    String type;
    String operator;
    String value;

    public SubscriptionData(String type, String operator, String value) {
        this.type = type;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s);", type, operator, value);
    }
}