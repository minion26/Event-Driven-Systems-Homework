package org.example.util;

import java.util.List;
import java.util.Random;

public class ListUtil {
    public static <T> T RandomFrom(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
