package org.khpi.diag.systems.lab1;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.khpi.diag.systems.lab1.model.Indication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@UtilityClass
public class Matrices {

    public static List<List<Double>> buildRelationMatrix(Map<Indication, List<Double>> indicationsMap,
                                                         BiFunction<List<Double>, List<Double>, Double> coefficient) {

        List<Indication> unprocessedIndications = Arrays.stream(Indication.values()).collect(Collectors.toList());
        List<List<Double>> result = new ArrayList<>(unprocessedIndications.size());

        do {
            Indication x1Indication = unprocessedIndications.get(0);
            List<Double> x1Values = indicationsMap.get(x1Indication);

            List<Double> row = unprocessedIndications.stream()
                    .map(indicationsMap::get)
                    .map(x2Values -> coefficient.apply(x1Values, x2Values))
                    .collect(Collectors.toList());

            unprocessedIndications.remove(x1Indication);
            result.add(row);

        } while (CollectionUtils.isNotEmpty(unprocessedIndications));

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
}
