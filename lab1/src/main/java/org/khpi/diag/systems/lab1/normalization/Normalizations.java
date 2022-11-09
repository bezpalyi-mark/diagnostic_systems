package org.khpi.diag.systems.lab1.normalization;

import java.util.List;
import java.util.stream.Collectors;

public class Normalizations {

    private Normalizations() {
    }

    public static List<Double> unitCube(List<Double> indicationValues) {
        double sum = indicationValues.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double center = sum / indicationValues.size();

        double radius = indicationValues.stream()
                .map(value -> Math.abs(value - center))
                .mapToDouble(Double::doubleValue)
                .max()
                .orElseThrow(() -> new IllegalStateException("Argument list is empty"));

        return indicationValues.stream()
                .map(value -> (value - center) / radius)
                .collect(Collectors.toList());
    }

}
