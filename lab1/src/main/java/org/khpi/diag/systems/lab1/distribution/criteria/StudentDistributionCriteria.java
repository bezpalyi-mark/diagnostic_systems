package org.khpi.diag.systems.lab1.distribution.criteria;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;

import java.util.ArrayList;
import java.util.List;

public class StudentDistributionCriteria {

    private final TDistribution tDistribution;
    private final int degreesOfFreedom;

    public StudentDistributionCriteria(int degreesOfFreedom) {
        this.tDistribution = new TDistributionImpl(degreesOfFreedom);
        this.degreesOfFreedom = degreesOfFreedom;
    }

    public List<List<Integer>> testRelationMatrix(List<List<Double>> relationMatrix, double p) throws MathException {

        List<List<Integer>> result = new ArrayList<>(relationMatrix.size());

        for (List<Double> row : relationMatrix) {
            List<Integer> testedRow = new ArrayList<>(row.size());

            for (Double value : row) {
                double tCritical = (Math.abs(value) * Math.sqrt(degreesOfFreedom)) / (Math.sqrt(1 - Math.pow(value, 2)));

                testedRow.add((int)(value / Math.abs(value)) * testHypothesis(p, tCritical));
            }

            result.add(testedRow);
        }

        return result;
    }

    private int testHypothesis(double p, double tCritical) throws MathException {
        double studentCritical = tDistribution.inverseCumulativeProbability(p);
        return tCritical < studentCritical
                ? 0
                : 1;
    }
}
