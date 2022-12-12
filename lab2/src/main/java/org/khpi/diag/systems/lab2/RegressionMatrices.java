package org.khpi.diag.systems.lab2;

import org.apache.commons.collections4.ListUtils;
import org.khpi.diag.systems.lab1.Matrices;
import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab2.model.Factor;
import org.khpi.diag.systems.lab2.model.Patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

public class RegressionMatrices {

    private static final String SIZE_ERROR_MESSAGE = "Sizes must be equals";
    private static final DoubleUnaryOperator ROUND_TO_3_DECIMAL_PLACES = doubleValue -> Math.round(doubleValue * 1000.0) / 1000.0;

    private RegressionMatrices() {
    }

    public static List<List<Double>> calculateXMatrix(List<Patient> patients) {
        Map<Factor, List<Double>> factorsMap = getFactorsMap(patients);

        List<List<Double>> result = new ArrayList<>(factorsMap.size() + 1);

        List<Double> factorsSums = new ArrayList<>(Factor.values().length);

        for (Factor factor : Factor.values()) {
            double sum = factorsMap.get(factor).stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            factorsSums.add(sum);
        }

        List<List<Double>> columns = Matrices.buildSymmetricRelations(factorsMap, RegressionMatrices::sumOfMultiplies);

        for (int i = 0; i < columns.size(); i++) {
            List<Double> column = columns.get(i);
            column.add(0, factorsSums.get(i));
        }

        List<Double> firstColumn = new ArrayList<>(factorsMap.size());
        firstColumn.add((double) patients.size());
        firstColumn.addAll(factorsSums);

        result.add(firstColumn);
        result.addAll(columns);

        return result;
    }

    private static Double sumOfMultiplies(List<Double> x1Values, List<Double> x2Values) {
        if (x1Values.size() != x2Values.size()) {
            throw new IllegalArgumentException("Sizes must be equal");
        }

        double result = 0;

        for (int i = 0; i < x1Values.size(); i++) {
            result += x1Values.get(i) * x2Values.get(i);
        }

        return result;
    }

    private static Map<Factor, List<Double>> getFactorsMap(List<Patient> patients) {
        Map<Factor, List<Double>> factorsMap = new EnumMap<>(Factor.class);
        Arrays.stream(Factor.values()).forEach(factor -> factorsMap.put(factor, new ArrayList<>()));

        for (Patient patient : patients) {
            Map<Factor, Double> patientFactors = patient.getFactors();
            patientFactors.forEach((factor, value) -> factorsMap.get(factor).add(value));
        }

        return factorsMap;
    }

    public static List<Double> calculateYMatrix(List<Patient> patients) {
        List<Double> result = new ArrayList<>();

        Map<Factor, List<Double>> factorsMap = getFactorsMap(patients);

        List<Map.Entry<Indication, Double>> responseList = patients.stream()
                .map(Patient::getResponse)
                .collect(Collectors.toList());

        double firstCell = responseList.stream()
                .map(Map.Entry::getValue)
                .mapToDouble(Double::doubleValue)
                .sum();

        result.add(firstCell);

        factorsMap.forEach((factor, factorValues) -> {
            double cellValue = 0;

            for (int i = 0; i < factorValues.size(); i++) {
                cellValue += factorValues.get(i) * responseList.get(i).getValue();
            }

            result.add(cellValue);
        });

        return result;
    }

    public static List<Double> gaussianElimination(List<List<Double>> xMatrix, List<Double> yMatrix) {
        List<Double> unifiedColumnValues = xMatrix.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Double> unifiedRowValues = changeLogicalDirection(unifiedColumnValues, xMatrix.size(), yMatrix.size());
        List<List<Double>> rows = new ArrayList<>(ListUtils.partition(unifiedRowValues, xMatrix.size()));

        List<List<Double>> extendedMatrixRows = joinMatrices(rows, yMatrix);

        List<List<Double>> echelonForm = forwardGaussElimination(extendedMatrixRows);

        List<List<Double>> echelonFormWithoutZeros = echelonForm.stream()
                .map(list -> list.stream()
                        .filter(value -> value != 0)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        return gaussBackSubstitution(echelonFormWithoutZeros);
    }

    private static List<List<Double>> forwardGaussElimination(List<List<Double>> extendedMatrixRows) {
        List<List<Double>> matrixRows = new ArrayList<>(extendedMatrixRows);

        for (int columnIndex = 0; columnIndex < matrixRows.size(); columnIndex++) {

            int indexWithNonZeroElement = -1;

            for (int rowIndex = columnIndex; indexWithNonZeroElement < 0; rowIndex++) {
                if (matrixRows.get(rowIndex).get(columnIndex) > 0) {
                    indexWithNonZeroElement = rowIndex;
                }
            }

            if (columnIndex != indexWithNonZeroElement) {
                Collections.swap(matrixRows, columnIndex, indexWithNonZeroElement);
            }

            List<Double> currentRow = matrixRows.get(columnIndex);

            for (int rowIndex = columnIndex + 1; rowIndex < matrixRows.size(); rowIndex++) {
                Double currentElement = currentRow.get(columnIndex);

                List<Double> rowBelow = matrixRows.get(rowIndex);
                Double elementBelow = rowBelow.get(columnIndex);

                double coefficient = elementBelow / currentElement;

                List<Double> multipliedRow = currentRow.stream()
                        .map(curValue -> curValue * coefficient)
                        .map(ROUND_TO_3_DECIMAL_PLACES::applyAsDouble)
                        .collect(Collectors.toList());

                List<Double> changedRow = multipliedRow.get(columnIndex) > elementBelow
                        ? combineRows(multipliedRow, rowBelow, Double::sum)
                        : combineRows(rowBelow, multipliedRow, (a, b) -> a - b);

                matrixRows.set(rowIndex, changedRow);
            }
        }

        return matrixRows;
    }

    private static List<Double> combineRows(List<Double> first, List<Double> second, DoubleBinaryOperator combiner) {

        if (first.size() != second.size()) {
            throw new IllegalArgumentException(SIZE_ERROR_MESSAGE);
        }

        List<Double> result = new ArrayList<>(first.size());

        for (int i = 0; i < first.size(); i++) {
            double sum = combiner.applyAsDouble(first.get(i), second.get(i));
            result.add(ROUND_TO_3_DECIMAL_PLACES.applyAsDouble(sum));
        }

        return result;
    }

    private static List<Double> gaussBackSubstitution(List<List<Double>> echelonForm) {
        Collections.reverse(echelonForm);

        if (echelonForm.get(0).size() != 2) {
            throw new IllegalStateException("Last row must be with size 2");
        }

        List<Double> reversedCoefficients = new ArrayList<>();

        for (List<Double> matrixRow : echelonForm) {
            LinkedList<Double> row = new LinkedList<>(matrixRow);
            Collections.reverse(row);

            double response = Objects.requireNonNull(row.poll());
            double sumWithDeterminedCoefficients = 0;

            for (Double coefficient : reversedCoefficients) {
                sumWithDeterminedCoefficients += Objects.requireNonNull(row.poll()) * coefficient;
            }

            double coefficient = (response - sumWithDeterminedCoefficients) / Objects.requireNonNull(row.poll());
            reversedCoefficients.add(coefficient);
        }

        Collections.reverse(reversedCoefficients);

        return reversedCoefficients;
    }

    private static List<List<Double>> joinMatrices(List<List<Double>> xMatrix, List<Double> yMatrix) {

        if (xMatrix.size() != yMatrix.size()) {
            throw new IllegalArgumentException(SIZE_ERROR_MESSAGE);
        }

        List<List<Double>> result = new ArrayList<>(xMatrix.size());

        for (int i = 0; i < xMatrix.size(); i++) {
            List<Double> fullRow = new ArrayList<>(xMatrix.get(i));
            fullRow.add(yMatrix.get(i));

            result.add(fullRow);
        }

        return result;
    }

    private static List<Double> changeLogicalDirection(List<Double> values, int rowLength, int columnsLength) {
        List<Double> viceVersaDirection = new ArrayList<>(values.size() / 4);
        List<List<Double>> forwardDirection = ListUtils.partition(values, rowLength);

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnsLength; j++) {
                Double value = forwardDirection.get(j).get(i);
                viceVersaDirection.add(value);
            }

        }

        return viceVersaDirection;
    }

    public static Double squaresResidualSum(List<List<Double>> xMatrix, List<Double> yMatrix, List<Double> coefficients) {
        double result = 0;

        for (int i = 0; i < yMatrix.size(); i++) {
            List<Double> row = xMatrix.get(i);
            Double yElement = yMatrix.get(i);
            Double a0 = coefficients.get(0);

            double xMulASum = 0;

            for (int j = 1; j < row.size(); j++) {
                xMulASum += row.get(j) * coefficients.get(j);
            }

            result += Math.pow(yElement - a0 - xMulASum, 2);
        }

        return result;
    }
}
