package org.khpi.diag.systems.lab2.writer;

import java.util.List;
import java.util.Map;

public class ConsoleWriter {

    private ConsoleWriter() {
    }

    public static void printMatricesWithCoefficients(Map<Integer, List<List<Double>>> diagnosisToXMatrix,
                                                     Map<Integer, List<Double>> diagnosisToYMatrix,
                                                     Map<Integer, List<Double>> coefficients,
                                                     Map<Integer, Double> predictedVariableVariance) {

        for (Map.Entry<Integer, List<List<Double>>> entry : diagnosisToXMatrix.entrySet()) {
            Integer key = entry.getKey();
            List<List<Double>> value = entry.getValue();
            System.out.println("Diagnosis #" + key);

            System.out.println("X Matrix: ");

            for (List<Double> row : value) {
                System.out.printf("| ");
                for (Double aDouble : row) {
                    System.out.printf("%8s | ", aDouble);
                }
                System.out.println();
            }

            System.out.println();

            System.out.println("Y Matrix: ");
            List<Double> yValues = diagnosisToYMatrix.get(key);
            yValues.forEach(yValue -> System.out.printf("| %8.8s |\n", yValue));

            System.out.println();

            List<Double> diagnosisCoefficients = coefficients.get(key);

            System.out.printf("| ");

            for (int i = 0; i < diagnosisCoefficients.size(); i++) {
                System.out.printf("a%d: %s | ", i, diagnosisCoefficients.get(i));
            }

            System.out.println();
            System.out.println();

            System.out.println("Sigma squared (σ^2): " + predictedVariableVariance.get(key));

            System.out.println("------------------------------------------------------------------");
        }

        System.out.println();
    }
}
