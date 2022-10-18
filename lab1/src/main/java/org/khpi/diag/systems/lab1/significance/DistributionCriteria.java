package org.khpi.diag.systems.lab1.significance;

import lombok.Builder;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;

public class DistributionCriteria {

    private final int sizeN;
    private final double probability;
    private final int degreesOfFreedom;
    private final NormalDistribution normalDistribution;
    private final TDistribution studentDistribution;

    @Builder
    public DistributionCriteria(int sizeN, double probability) {
        this.sizeN = sizeN;
        this.probability = probability;
        this.degreesOfFreedom = sizeN - 2;
        this.normalDistribution = new NormalDistributionImpl();
        this.studentDistribution = new TDistributionImpl(degreesOfFreedom);
    }

    public int normal(double value) {
        try {
            double zCritical = (Math.abs(value) * Math.sqrt(9.0 * sizeN * (sizeN - 1.0)))
                    / (Math.sqrt(2.0 * (2.0 * sizeN + 5.0)));

            double zProbability = zCritical > 0
                    ? 1.0 - normalDistribution.cumulativeProbability(-zCritical, zCritical)
                    : 1.0 - normalDistribution.cumulativeProbability(zCritical, -zCritical);

            return zProbability < (1.0 - probability)
                    ? 1
                    : 0;
        } catch (MathException e) {
            throw new IllegalStateException(e);
        }
    }

    public int student(double value) {
        try {
            double tCritical = (Math.abs(value) * Math.sqrt(degreesOfFreedom)) / (Math.sqrt(1 - Math.pow(value, 2)));

            double studentCritical = studentDistribution.inverseCumulativeProbability(probability);

            return tCritical < studentCritical
                    ? 0
                    : 1;
        } catch (MathException e) {
            throw new IllegalStateException(e);
        }
    }
}
