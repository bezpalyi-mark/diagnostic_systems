package org.khpi.diag.systems.lab1.writer;

import org.khpi.diag.systems.lab1.model.Indication;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConsoleWriter {
    public static void printEDT(Map<Indication, List<Double>> indications) {
        System.out.println("EXPERIMENTAL DATA TABLE");

        Arrays.stream(Indication.values()).forEach(indication -> System.out.printf(" %18s |", indication.getShortIndicationName()));

        System.out.println();

        int size = indications.get(Indication.LEUKOCYTES_NUMBER).size();

        for (int i = 0; i < size; i++) {
            for (Indication sign : Indication.values()) {
                System.out.printf(" %18s |", indications.get(sign).get(i));
            }
            System.out.println();
        }

        System.out.println();
    }

    public static void printRelationMatrix(String name, List<List<Double>> matrix) {
        System.out.printf("%S RELATION MATRIX%n", name);
        matrix.forEach(row -> {
            row.forEach(value -> System.out.printf("%7.4f |", value));
            System.out.println();
        });
        System.out.println();
    }

    public static void printTestedRelationMatrix(String name, int degreesOfFreedom, double p, List<List<Integer>> matrix) {
        System.out.printf("TESTED %S RELATION MATRIX%n", name);
        System.out.printf("Degrees of freedom: %d, P: %.2f%n", degreesOfFreedom, p);
        matrix.forEach(row -> {
            row.forEach(value -> System.out.printf("%4d |", value));
            System.out.println();
        });
        System.out.println();
    }
}
