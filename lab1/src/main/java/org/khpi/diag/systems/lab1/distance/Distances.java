package org.khpi.diag.systems.lab1.distance;

import java.util.ArrayList;
import java.util.List;

public class Distances {
    public static double chebyshev(List<Double> x1Vector, List<Double> x2Vector) {
        if (x1Vector.size() != x2Vector.size()) {
            throw new IllegalArgumentException("Vectors must be with the same size");
        }

        List<Double> subs = new ArrayList<>();

        for (int i = 0; i < x1Vector.size(); i++) {
            subs.add(Math.abs(x1Vector.get(i) - x2Vector.get(i)));
        }

        return subs.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElseThrow(() -> new IllegalStateException("Values are not present"));
    }
}
