package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BasicBolt  {

    private final BufferedWriter writer;

    public BasicBolt() throws IOException {
        // Generate a unique file name using a timestamp
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        this.writer = new BufferedWriter(new FileWriter("tuples_" + timestamp + ".txt"));
    }

    public synchronized void execute(Object tuple) {
        System.out.println("Received: " + tuple);
        try {
            writer.write(tuple.toString());
            writer.newLine();
            writer.flush(); // Ensure data is written to the file
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close() throws IOException {
        writer.close();
    }


}
