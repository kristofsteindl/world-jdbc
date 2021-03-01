package com.ksteindl.worldjdbc.util;

import com.ksteindl.worldjdbc.model.Printable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class PrintUtils {

    private static final String SEPARATOR = "------------------------";


    public static <T extends Printable> void printResults(List<T> printables, Function<T, String> printableProducer) {
        System.out.println(SEPARATOR);
        Stream.iterate(1, i -> i + 1)
                .limit(printables.size())
                .forEach(i -> System.out.println(i + " - " + printableProducer.apply(printables.get(i - 1))));
        System.out.println(SEPARATOR + "\n");
    }

    public static void printSuccesOperation(String message) {
        System.out.println(SEPARATOR);
        System.out.println("Operation was successful.");
        System.out.println(message);
        System.out.println(SEPARATOR + "\n");
    }
}
