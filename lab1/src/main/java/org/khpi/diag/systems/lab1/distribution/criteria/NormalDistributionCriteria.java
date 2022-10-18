package org.khpi.diag.systems.lab1.distribution.criteria;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.ArrayList;
import java.util.List;

public class NormalDistributionCriteria {

    private final NormalDistribution normalDistribution;

    public NormalDistributionCriteria() {
        normalDistribution = new NormalDistributionImpl();
    }

    public List<List<Integer>> testRelationMatrix(List<List<Double>> relationMatrix, int sizeN, double p) throws MathException {
        List<List<Integer>> result = new ArrayList<>(relationMatrix.size());

        for (List<Double> row : relationMatrix) {
            List<Integer> testedRow = new ArrayList<>(row.size());

            for (Double value : row) {
                double zCritical = (Math.abs(value) * Math.sqrt(9.0 * sizeN * (sizeN - 1.0)))
                        / (Math.sqrt(2.0 * (2.0 * sizeN + 5.0)));

                double zProbability = zCritical > 0
                        ? 1.0 - normalDistribution.cumulativeProbability(-zCritical, zCritical)
                        : 1.0 - normalDistribution.cumulativeProbability(zCritical, -zCritical);

                int significant = zProbability < (1.0 - p)
                        ? 1
                        : 0;

                testedRow.add((int)(value / Math.abs(value)) * significant);
            }

            result.add(testedRow);
        }

        return result;
    }
}
