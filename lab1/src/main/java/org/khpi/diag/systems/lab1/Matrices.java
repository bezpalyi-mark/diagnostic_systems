package org.khpi.diag.systems.lab1;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.khpi.diag.systems.lab1.model.Indication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.DoubleToIntFunction;
import java.util.stream.Collectors;

@UtilityClass
public class Matrices {

    public static Map<Indication, List<Double>> calculateRanks(Map<Indication, List<Double>> indicationsMap) {
        Map<Indication, List<Double>> ranksMatrix = new EnumMap<>(Indication.class);

        indicationsMap.forEach((indication, forwardList) -> {
            List<Double> reverseList = forwardList.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            List<Double> rankColumn = new ArrayList<>();
            Map<Double, Double> checkedValues = new HashMap<>();

            forwardList.forEach(value -> {
                Double calculatedPreviously = checkedValues.get(value);

                if (calculatedPreviously == null) {
                    double rank = getRank(value, reverseList);
                    rankColumn.add(rank);
                    checkedValues.put(value, rank);
                } else {
                    rankColumn.add(calculatedPreviously);
                }
            });

            ranksMatrix.put(indication, rankColumn);
        });

        return ranksMatrix;
    }


    private static double getRank(double x, List<Double> column) {
        int firstIndex = column.indexOf(x) + 1;
        long count = column.stream().filter(value -> value == x).count();

        if (count == 1) {
            return firstIndex;
        }

        int lastIndex = column.lastIndexOf(x) + 1;

        return (lastIndex + firstIndex) / 2.0;
    }

    public List<List<Integer>> testSignificanceByDistribution(List<List<Double>> matrix, DoubleToIntFunction distributionCriteria) {
        List<List<Integer>> result = new ArrayList<>(matrix.size());

        for (List<Double> row : matrix) {
            List<Integer> testedRow = new ArrayList<>(row.size());

            for (Double value : row) {
                int significance = distributionCriteria.applyAsInt(value);
                testedRow.add((int) (value / Math.abs(value)) * significance);
            }

            result.add(testedRow);
        }

        return result;
    }

    public List<List<Double>> getDistanceMatrix(Map<Indication, List<Double>> indications,
                                                BiFunction<List<Double>, List<Double>, Double> distanceMethod) {

        Map<Integer, List<Double>> objects = splitIndicationsByObject(new ArrayList<>(indications.values()));

        return buildSymmetricRelations(objects, distanceMethod);
    }

    private Map<Integer, List<Double>> splitIndicationsByObject(List<List<Double>> matrixColumns) {

        int objectsCount = matrixColumns.get(0).size();

        Map<Integer, List<Double>> result = new HashMap<>(objectsCount);

        for (int i = 0; i < objectsCount; i++) {
            ArrayList<Double> row = new ArrayList<>();

            for (List<Double> column : matrixColumns) {
                row.add(column.get(i));
            }

            result.put(i, row);
        }

        return result;
    }

    public static <T> List<List<Double>> buildSymmetricRelations(Map<T, List<Double>> data,
                                                                  BiFunction<List<Double>, List<Double>, Double> mapper) {

        List<T> uncheckedObjectKeys = new ArrayList<>(data.keySet());
        List<List<Double>> result = new ArrayList<>(uncheckedObjectKeys.size());

        do {
            T objectKey = uncheckedObjectKeys.get(0);
            List<Double> x1Values = data.get(objectKey);

            List<Double> row = uncheckedObjectKeys.stream()
                    .map(data::get)
                    .map(x2Values -> mapper.apply(x1Values, x2Values))
                    .collect(Collectors.toList());

            uncheckedObjectKeys.remove(objectKey);
            result.add(row);

        } while (CollectionUtils.isNotEmpty(uncheckedObjectKeys));

        return reflectAlongMainDiagonal(result);
    }

    private static List<List<Double>> reflectAlongMainDiagonal(List<List<Double>> matrixRows) {
        List<List<Double>> matrixColumns = new ArrayList<>(matrixRows.size());
        matrixRows.forEach(row -> matrixColumns.add(new ArrayList<>(row.size())));

        for (int j = 0; j < matrixRows.size(); j++) {
            List<Double> row = matrixRows.get(j);
            for (int i = 1, k = j; i < row.size(); i++, k++) {
                List<Double> column = matrixColumns.get(k);
                Double value = row.get(i);
                column.add(value);
            }
        }

        matrixColumns.add(0, new ArrayList<>());

        for (int i = 0; i < matrixRows.size(); i++) {
            matrixColumns.get(i).addAll(matrixRows.get(i));
        }

        return matrixColumns;
    }
}
