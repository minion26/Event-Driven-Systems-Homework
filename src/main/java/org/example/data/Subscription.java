package org.example.data;

import java.util.ArrayList;
import java.util.List;

public class Subscription {
    private List<SubscriptionData> subscriptionData = new ArrayList<>();

    public Subscription() {}

    public void addData(SubscriptionData data) {
        subscriptionData.add(data);
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();

        for (SubscriptionData data : subscriptionData) {
            value.append(data.toString());
        }

        return String.format("{%s}", value);
    }
}

