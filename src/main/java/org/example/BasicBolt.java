package org.example;



import java.util.Map;

public class BasicBolt  {


    public void execute(Object tuple) {
        System.out.println("Received: " + tuple);

    }


}
