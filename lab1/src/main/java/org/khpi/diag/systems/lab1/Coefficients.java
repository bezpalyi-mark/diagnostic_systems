package org.khpi.diag.systems.lab1;

import lombok.experimental.UtilityClass;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class Coefficients {
    public static double pearson(List<Double> x1Vector, List<Double> x2Vector) {
        int size = x1Vector.size();

        double xSum = 0;
        double ySum = 0;
        double xSquareSum = 0;
        double ySquareSum = 0;
        double sumXY = 0;

        for (int i = 0; i < x1Vector.size(); i++) {
            Double x = x1Vector.get(i);
            Double y = x2Vector.get(i);
            xSum += x;
            ySum += y;
            xSquareSum += x * x;
            ySquareSum += y * y;
            sumXY += x * y;
        }

        double denominator = ((size * xSquareSum) - Math.pow(xSum, 2)) * ((size * ySquareSum) - Math.pow(ySum, 2));

        return ((size * sumXY) - (xSum * ySum)) / Math.sqrt(denominator);
    }

    public static double kendall(List<Double> x1Vector, List<Double> x2Vector) {
        List<Map.Entry<Double, Double>> x1ToX2Entries = new ArrayList<>();

        for (int i = 0; i < x1Vector.size(); i++) {
            AbstractMap.SimpleImmutableEntry<Double, Double> x1ToX2 = new AbstractMap.SimpleImmutableEntry<>(x1Vector.get(i), x2Vector.get(i));
            x1ToX2Entries.add(x1ToX2);
        }

        List<Double> x2Sorted = x1ToX2Entries.stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<Integer> concordant = new ArrayList<>();
        List<Integer> discordant = new ArrayList<>();

        for (int i = 0; i < x2Sorted.size(); i++) {
            Double testValue = x2Sorted.get(i);

            int concordantCount = (int) x2Sorted.stream()
                    .skip(i)
                    .filter(valueBelow -> testValue < valueBelow)
                    .count();

            int discordantCount = (int) x2Sorted.stream()
                    .skip(i)
                    .filter(valueBelow -> testValue > valueBelow)
                    .count();

            concordant.add(concordantCount);
            discordant.add(discordantCount);
        }

        double c = concordant.stream()
                .mapToDouble(Double::valueOf)
                .sum();

        double d = discordant.stream()
                .mapToDouble(Double::valueOf)
                .sum();

        return (c - d) / (c + d);
    }
}
