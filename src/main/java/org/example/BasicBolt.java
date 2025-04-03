package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicBolt  {
    BufferedWriter writer;

    public BasicBolt() {
        try {
            writer = new BufferedWriter(new FileWriter("list.txt"));

            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(Object tuple) {
        try {
            System.out.println(tuple.toString());

            writer.append(tuple.toString())
                    .append("\n")
                    .flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
